package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virt.pseudopackets.handlers.ClientPacketHandler;
import ch.virt.pseudopackets.handlers.ServerPacketHandler;
import ch.virt.pseudopackets.server.Server;
import ch.virtbad.serint.client.Serint;
import ch.virtbad.serint.client.networking.packets.KickPaket;
import ch.virtbad.serint.client.networking.packets.LoginPacket;
import ch.virtbad.serint.client.networking.packets.PingPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

/**
 * This class handles all packet traffic
 * @author Virt
 */
@Slf4j
public class Communications extends ClientPacketHandler {

    @Getter
    private static Communications instance;
    /**
     * Loads the communications
     */
    public static void load(){
        log.info("Creating the Communications");
        instance = new Communications();
    }

    @Setter
    private Client client; // Is getting set automatically after server creation

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

    }

    public void handle(PingPacket packet) {
        long diff = System.nanoTime() - packet.getCurrentTimeNanos();
        log.info("Received Ping with latency: {}ms", diff / 10E6f);
    }

    public void handle(KickPaket paket) throws IOException {
        log.warn("Kicked from server because of: " + paket.getReason());
        client.close();
    }

}
