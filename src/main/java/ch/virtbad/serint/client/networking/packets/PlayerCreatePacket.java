package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when a player has been created
 * @author Virt
 */
@Getter
public class PlayerCreatePacket extends Packet{
    private int playerId;

    private String name;
    private int color;

    /**
     * Constructor
     * @param playerId id of the player
     * @param name name of the player
     * @param color color of the player
     */
    public PlayerCreatePacket(int playerId, String name, int color) {
        super(30);
        this.playerId = playerId;
        this.name = name;
        this.color = color;
    }
}
