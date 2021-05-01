package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

@Getter
public class NotJoinedPacket extends Packet {
    private String reason;

    /**
     * Constructor
     */
    public NotJoinedPacket(String reason) {
        super(12);
        this.reason = reason;
    }
}
