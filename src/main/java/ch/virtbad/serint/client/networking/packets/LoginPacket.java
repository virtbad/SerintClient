package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent to login at the server
 * @author Virt
 */
public class LoginPacket extends Packet {

    @Getter
    private String version;

    /**
     * Constructor
     * @param version Version of the client
     */
    public LoginPacket(String version) {
        super(1);
        this.version = version;
    }
}
