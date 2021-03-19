package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * @author Virt
 */
@Getter
public class PlayerCreatePacket extends Packet{
    private int playerId;

    private String name;
    private int color;

    public PlayerCreatePacket(int playerId, String name, int color) {
        super(30);
        this.playerId = playerId;
        this.name = name;
        this.color = color;
    }
}
