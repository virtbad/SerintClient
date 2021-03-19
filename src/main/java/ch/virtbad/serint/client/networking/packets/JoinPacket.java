package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when a client tries to join the game
 * @author Virt
 */
public class JoinPacket extends Packet {

    @Getter
    private String name;
    @Getter
    private int color;

    /**
     * Constructor
     * @param name name of the player
     * @param color color of the player
     */
    public JoinPacket(String name, int color) {
        super(10);
        this.name = name;
        this.color = color;
    }
}
