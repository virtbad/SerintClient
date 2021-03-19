package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when a player has left / is destroyed
 * @author Virt
 */
@Getter
public class PlayerDestroyPacket extends Packet {
    int playerId;

    /**
     * Constructor
     * @param playerId id of destroyed player
     */
    public PlayerDestroyPacket(int playerId) {
        super(31);
        this.playerId = playerId;
    }
}

