package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This Packet is used for ping communication
 * @author Virt
 */
public class PingPacket extends Packet {
    @Getter
    private long currentTimeNanos;

    /**
     * Constructor
     * @param currentTimeNanos current system time in nano seconds
     */
    public PingPacket(long currentTimeNanos) {
        super(0);
        this.currentTimeNanos = currentTimeNanos;
    }
}
