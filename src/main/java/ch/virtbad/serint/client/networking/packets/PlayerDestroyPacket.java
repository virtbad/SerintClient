package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * @author Virt
 */
@Getter
public class PlayerDestroyPacket extends Packet {
    int playerId;

    public PlayerDestroyPacket(int playerId) {
        super(31);
        this.playerId = id;
    }
}
