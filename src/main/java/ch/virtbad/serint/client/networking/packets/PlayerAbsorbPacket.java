package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when a player absorbs another player
 * @author Virt
 */
@Getter
public class PlayerAbsorbPacket extends Packet {

    private int playerId;

    /**
     * Constructor
     * @param playerId id of the absorbed player
     */
    public PlayerAbsorbPacket(int playerId) {
        super(41);
        this.playerId = playerId;
    }
}
