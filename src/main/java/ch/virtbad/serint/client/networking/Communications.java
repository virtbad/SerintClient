package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virt.pseudopackets.handlers.ClientPacketHandler;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.networking.packets.LoggedInPacket;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.Serint;
import ch.virtbad.serint.client.networking.packets.KickPaket;
import ch.virtbad.serint.client.networking.packets.LoginPacket;
import ch.virtbad.serint.client.networking.packets.PingPacket;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This class handles all packet traffic
 * @author Virt
 */
@Slf4j
public class Communications extends ClientPacketHandler {

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

    public void handle(LoggedInPacket packet){
        Globals.getNetwork().setServerName(packet.getName());
        Globals.getNetwork().setServerDescription(packet.getDescription());
        Globals.getNetwork().setServerVersion(packet.getVersion());

        log.info("Successfully connected to {} with Description \"{}\" on Version {}", packet.getName(), packet.getDescription(), packet.getVersion());
    }

}
