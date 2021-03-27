package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import ch.virtbad.serint.client.game.player.PlayerAttributes;
import lombok.Getter;

/**
 * This packet is used to transmit new attributes
 * @author Virt
 */
@Getter
public class PlayerAttributePacket extends Packet {

    private int player;
    private PlayerAttributes attributes;

    /**
     * Constructor
     * @param attributes attributes to transmit
     */
    public PlayerAttributePacket(PlayerAttributes attributes, int player) {
        super(34);

        this.attributes = attributes;
        this.player = player;
    }
}
