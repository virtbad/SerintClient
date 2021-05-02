package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent to a player when one loses
 * AND YES THIS CLASS HAS A TYPO IN ITS NAME
 */
public class LoosePacket extends Packet {

    @Getter
    private String absorberName;

    /**
     * Constructor
     * @param absorberName name of the player who absorbed it
     */
    public LoosePacket(String absorberName) {
        super(55);
        this.absorberName = absorberName;
    }
}
