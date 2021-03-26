package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when a new item is created
 * @author Virt
 */
@Getter
public class ItemCreatePacket extends Packet {

    private int itemType;
    private int itemId;

    private float x, y;

    /**
     * Constructor
     * @param itemType type of the item
     * @param itemId id of the item
     * @param x x coordinate of item
     * @param y y coordinate of item
     */
    public ItemCreatePacket(int itemType, int itemId, float x, float y) {
        super(21);

        this.itemType = itemType;
        this.itemId = itemId;
        this.x = x;
        this.y = y;
    }
}
