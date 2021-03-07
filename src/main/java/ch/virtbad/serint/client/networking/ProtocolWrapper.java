package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.packets.Protocol;
import ch.virtbad.serint.client.networking.packets.KickPaket;
import ch.virtbad.serint.client.networking.packets.LoggedInPacket;
import ch.virtbad.serint.client.networking.packets.LoginPacket;
import ch.virtbad.serint.client.networking.packets.PingPacket;
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

        protocol.addPacket(PingPacket.class, 0);
        protocol.addPacket(LoginPacket.class, 1);
        protocol.addPacket(LoggedInPacket.class, 2);
        protocol.addPacket(KickPaket.class, 3);

    }
}
