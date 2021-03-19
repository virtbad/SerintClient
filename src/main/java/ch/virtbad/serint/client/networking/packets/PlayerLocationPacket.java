package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when the location of a player needs to be updated
 * @author Virt
 */
@Getter
public class PlayerLocationPacket extends Packet {

    private int playerId;

    private float x, y, velocityX, velocityY;

    /**
     * Constructor
     * @param playerId id of the player
     * @param x x position
     * @param y y position
     * @param velocityX x velocity
     * @param velocityY y velocity
     */
    public PlayerLocationPacket(int playerId, float x, float y, float velocityX, float velocityY) {
        super(32);
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
