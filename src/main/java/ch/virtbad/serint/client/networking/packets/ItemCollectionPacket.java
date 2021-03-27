package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when a player collects an item
 * @author Virt
 */
@Getter
public class ItemCollectionPacket extends Packet {

    private int itemId;

    /**
     * Constructor
     * @param itemId id of collected item
     */
    public ItemCollectionPacket(int itemId) {
        super(40);
        this.itemId = itemId;
    }
}
