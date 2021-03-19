package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when a client has joined the game
 * @author Virt
 */
public class JoinedPacket extends Packet {

    @Getter
    private int playerId;

    /**
     * Constructor
     * @param playerId assigned player id to the client
     */
    public JoinedPacket(int playerId) {
        super(11);
        this.playerId = playerId;
    }
}