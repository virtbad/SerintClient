package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * @author Virt
 */
public class JoinedPacket extends Packet {

    @Getter
    private int playerId;

    public JoinedPacket(int playerId) {
        super(11);
        this.playerId = playerId;
    }
}
