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
        protocol.addPacket(LoggedOutPacket.class, 4);

        // Game Connection Packets
        protocol.addPacket(JoinPacket.class, 10);
        protocol.addPacket(JoinedPacket.class, 11);
        protocol.addPacket(NotJoinedPacket.class, 12);

        // Environment Data Packets
        protocol.addPacket(MapPacket.class, 20);
        protocol.addPacket(ItemCreatePacket.class, 21);
        protocol.addPacket(ItemDestroyPacket.class, 22);

        // Player Transmission
        protocol.addPacket(PlayerCreatePacket.class, 30);
        protocol.addPacket(PlayerDestroyPacket.class, 31);
        protocol.addPacket(PlayerLocationPacket.class, 32);
        protocol.addPacket(PlayerAttributePacket.class, 34);

        // Interaction Transmission
        protocol.addPacket(PlayerAbsorbPacket.class, 41);
        protocol.addPacket(ItemCollectionPacket.class, 40);

        // Game State Transmission
        protocol.addPacket(GameStartPeakPacket.class, 50);
        protocol.addPacket(GameStartPacket.class, 51);
        protocol.addPacket(AbsorbedPacket.class, 52);
        protocol.addPacket(RespawnPacket.class, 53);
        protocol.addPacket(WinPacket.class, 54);
        protocol.addPacket(LoosePacket.class, 55);

    }
}
