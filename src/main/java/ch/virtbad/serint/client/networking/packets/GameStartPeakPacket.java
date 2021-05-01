package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet announces when a game is started
 */
public class GameStartPeakPacket extends Packet {

    @Getter
    private float delay;

    /**
     * Constructor
     * @param delay delay until start
     */
    public GameStartPeakPacket(float delay) {
        super(50);
        this.delay = delay;
    }
}
