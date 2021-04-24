package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when the initiating handshake fails, thus logging the client out
 */
public class LoggedOutPacket extends Packet {
    @Getter
    private String reason;

    /**
     * Constructor
     */
    public LoggedOutPacket(String reason) {
        super(4);
        this.reason = reason;
    }
}
