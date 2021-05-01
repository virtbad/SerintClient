package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.game.objects.positioning.MovedLocation;
import ch.virtbad.serint.client.networking.packets.*;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.Serint;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;

/**
 * This class handles all packet traffic
 *
 * @author Virt
 */
@Slf4j
public class Communications extends CustomClientPacketHandler {
    public static final int ESTABLISHING = 1;
    public static final int FAILED_ESTABLISH = 2;
    public static final int HANDSHAKE = 3;
    public static final int FAILED_HANDSHAKE = 4;
    public static final int IN = 5;
    public static final int JOINING = 6;
    public static final int FAILED_JOIN = 7;
    public static final int DONE = 8;

    @Getter @Setter
    private int status;
    @Getter @Setter
    private String statusInformation;
    @Getter
    private boolean connected;

    @Setter
    private Client client; // Is getting set automatically after server creation
    @Setter
    private Game game;

    private final BasicEvent kicked;

    /**
     * Creates the communication
     * @param kicked listener to call when kicked
     */
    public Communications(BasicEvent kicked) {

        this.kicked = kicked;
    }

    /**
     * Logs into the server
     */
    public void connect() {
        status = HANDSHAKE;
        client.sendPacket(new PingPacket(System.nanoTime())); // Send Test Ping
        client.sendPacket(new LoginPacket(Serint.VERSION)); // Login
    }

    @Override
    public void connected() {
        connected = true;
        log.info("Connected to server!");
    }

    @Override
    public void disconnected() {
        connected = false;
        log.info("Disconnected from server!");
    }


    // ----- Basic Connection Packets -----

    public void handle(PingPacket packet) {
        long diff = System.nanoTime() - packet.getCurrentTimeNanos();
        Globals.getNetwork().setServerPing(diff / 10E6f);
        log.info("Received Ping with latency: {}ms", diff / 10E6f);
    }

    public void handle(KickPaket packet) throws IOException {
        log.warn("Kicked from server because of: " + packet.getReason());
        client.close();

        statusInformation = packet.getReason();
        kicked.emit();
    }

    public void handle(LoggedInPacket packet) {
        Globals.getNetwork().setServerName(packet.getName());
        Globals.getNetwork().setServerDescription(packet.getDescription());
        Globals.getNetwork().setServerVersion(packet.getVersion());

        status = IN;

        log.info("Successfully connected to {} with Description \"{}\" on Version {}", packet.getName(), packet.getDescription(), packet.getVersion());
    }

    public void handle(LoggedOutPacket packet) {

        status = FAILED_HANDSHAKE;
        statusInformation = packet.getReason();

        disconnect();

        log.info("Failed handshake because of \"{}\"", packet.getReason());
    }


    // ----- Advanced Connection Packets -----

    public void handle(JoinedPacket packet) {
        log.info("Joined with Player id {}", packet.getPlayerId());
        game.joined(packet.getPlayerId(), new Color(packet.getColor()), packet.getName());
        status = DONE;
    }

    public void handle(NotJoinedPacket packet) {
        status = FAILED_JOIN;
        statusInformation = packet.getReason();

        disconnect();

        log.info("Failed to join because {}", packet.getReason());
    }


    // ----- Player Connection Packets -----

    public void handle(PlayerCreatePacket packet) {
        game.createPlayer(packet.getPlayerId(), packet.getColor(), packet.getName());
    }

    public void handle(PlayerDestroyPacket packet) {
        game.destroyPlayer(packet.getPlayerId());
    }

    public void handle(PlayerLocationPacket packet) {
        game.relocatePlayer(packet.getPlayerId(), packet.getX(), packet.getY(), packet.getVelocityX(), packet.getVelocityY());
    }

    public void handle(PlayerAttributePacket packet) {
        game.updatePlayerAttributes(packet.getPlayer(), packet.getAttributes());
    }


    // ----- Environment Packets -----

    public void handle(MapPacket packet) {
        game.createMap(packet.getMap());
    }

    public void handle(ItemCreatePacket packet) {
        game.createItem(packet.getItemId(), packet.getX(), packet.getY(), packet.getItemType());
    }

    public void handle(ItemDestroyPacket packet) {
        game.destroyItem(packet.getItemId());
    }


    // ----- Interaction Methods

    public void collectItem(int id) {
        client.sendPacket(new ItemCollectionPacket(id));
    }

    public void attackPlayer(int id) {
        client.sendPacket(new PlayerAbsorbPacket(id));
    }

    // ----- Game States -----

    public void handle(AbsorbedPacket packet) {
        game.absorbed(packet.getRespawnTime(), packet.getAbsorberName());
    }

    public void handle(RespawnPacket packet) {
        game.respawn();
    }

    public void handle(LoosePacket packet){
        game.loose(packet.getAbsorberName());
    }

    public void handle(WinPacket packet){
        game.win();
    }

    public void handle(GameStartPacket packet){
        game.start();
    }

    public void handle(GameStartPeakPacket packet){
        game.peakStart(packet.getDelay());
    }

    // ----- Other Methods -----

    /**
     * Joins the client using basic player information
     *
     * @param color color to join with
     * @param name  name to join with
     */
    public void join(Color color, String name) {
        status = JOINING;
        log.info("Joining the Game with Name: {}", name);
        client.sendPacket(new JoinPacket(name, color.getRGB()));
    }

    /**
     * Pushes a location as a player location onto the server
     *
     * @param location location to publish
     */
    public void pushPlayerLocation(MovedLocation location) {
        client.sendPacket(new PlayerLocationPacket(0, location.getPosX(), location.getPosY(), location.getVelocityX(), location.getVelocityY())); // We do not care about ids
    }

    public void disconnect(){
        if (!connected) return;
        log.info("Disconnecting from Server");

        try {
            client.close();
        } catch (IOException e) {
            log.warn("Failed to disconnect from Server");
            e.printStackTrace();
        }
    }

}
