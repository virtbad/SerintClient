package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when an Item is destroyed
 * @author Virt
 */
@Getter
public class ItemDestroyPacket extends Packet {

    private int itemId;

    /**
     * Constructor
     * @param itemId id of item
     */
    public ItemDestroyPacket(int itemId) {
        super(22);
        this.itemId = itemId;
    }
}
