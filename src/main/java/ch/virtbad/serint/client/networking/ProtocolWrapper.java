package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.packets.Protocol;
import ch.virtbad.serint.client.networking.packets.*;
import lombok.Getter;

/**
 * This class wraps the Protocol for its creation
 * @author Virt
 */
public class ProtocolWrapper {

    @Getter
    private final Protocol protocol;

    /**
     * Initializes a ProtocolWrapper and registers the packets
     */
    public ProtocolWrapper(){
        protocol = new Protocol();

        registerPackets();
    }

    /**
     * This method calls all register Packet methods to insert packets into the protocol.
     * Every Packet should be added here
     */
    private void registerPackets(){
        // Register Packets here

        // Moderation Packets
        protocol.addPacket(PingPacket.class, 0);
        protocol.addPacket(LoginPacket.class, 1);
        protocol.addPacket(LoggedInPacket.class, 2);
        protocol.addPacket(KickPaket.class, 3);

        // Game Connection Packets
        protocol.addPacket(JoinPacket.class, 10);
        protocol.addPacket(JoinedPacket.class, 11);

        // Game Content Packets
        protocol.addPacket(MapPacket.class, 20);

        // Player Transmission
        protocol.addPacket(PlayerCreatePacket.class, 30);
        protocol.addPacket(PlayerDestroyPacket.class, 31);
        protocol.addPacket(PlayerLocationPacket.class, 32);

    }
}
