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
    @Getter
    private int color;
    @Getter
    private String name;

    /**
     * Constructor
     * @param playerId assigned player id to the client
     * @param color color of player
     * @param name name of player
     */
    public JoinedPacket(int playerId, int color, String name) {
        super(11);
        this.playerId = playerId;
        this.color = color;
        this.name = name;
    }
}