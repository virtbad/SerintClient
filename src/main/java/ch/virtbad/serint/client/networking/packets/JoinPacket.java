package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * @author Virt
 */
public class JoinPacket extends Packet {

    @Getter
    private String name;
    @Getter
    private int color;

    public JoinPacket(String name, int color) {
        super(10);
        this.name = name;
        this.color = color;
    }
}
