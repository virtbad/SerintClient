package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virt.pseudopackets.handlers.ClientPacketHandler;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.game.positioning.MovedLocation;
import ch.virtbad.serint.client.networking.packets.*;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.Serint;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;

/**
 * This class handles all packet traffic
 * @author Virt
 */
@Slf4j
public class Communications extends CustomClientPacketHandler {

    @Setter
    private Client client; // Is getting set automatically after server creation
    @Setter
    private Game game;

    /**
     * Creates the communication
     */
    public Communications(){

    }

    @Override
    public void connected() {
        log.info("Connected to server!");
        client.sendPacket(new PingPacket(System.nanoTime())); // Send Test Ping
        client.sendPacket(new LoginPacket(Serint.VERSION)); // Login
    }

    @Override
    public void disconnected() {
        log.info("Disconnected from server!");
    }

    public void handle(PingPacket packet) {
        long diff = System.nanoTime() - packet.getCurrentTimeNanos();
        Globals.getNetwork().setServerPing(diff / 10E6f);
        log.info("Received Ping with latency: {}ms", diff / 10E6f);
    }

    public void handle(KickPaket packet) throws IOException {
        log.warn("Kicked from server because of: " + packet.getReason());
        client.close();
    }

    public void handle(LoggedInPacket packet) {
        Globals.getNetwork().setServerName(packet.getName());
        Globals.getNetwork().setServerDescription(packet.getDescription());
        Globals.getNetwork().setServerVersion(packet.getVersion());

        log.info("Successfully connected to {} with Description \"{}\" on Version {}", packet.getName(), packet.getDescription(), packet.getVersion());

        //TODO: Remove, this is only for Testing reasons
        join(Color.RED, "Test");
    }

    /**
     * Joins the client using basic player information
     * @param color color to join with
     * @param name name to join with
     */
    public void join(Color color, String name){
        log.info("Joining the Game with Name: {}", name);
        client.sendPacket(new JoinPacket(name, color.getRGB()));
    }

    public void handle(JoinedPacket packet){
        log.info("Joined with Player id {}", packet.getPlayerId());
        game.joined(packet.getPlayerId());
    }

    public void handle(PlayerCreatePacket packet){
        game.createPlayer(packet.getPlayerId(), packet.getColor(), packet.getName());
    }

    public void handle(PlayerDestroyPacket packet){
        game.destroyPlayer(packet.getPlayerId());
    }

    public void handle(PlayerLocationPacket packet){
        game.relocatePlayer(packet.getPlayerId(), packet.getX(), packet.getY(), packet.getVelocityX(), packet.getVelocityY());
    }

    public void pushPlayerLocation(MovedLocation location){
        client.sendPacket(new PlayerLocationPacket(0, location.getPosX(), location.getPosY(), location.getVelocityX(), location.getVelocityY())); // We do not care about ids
    }

}