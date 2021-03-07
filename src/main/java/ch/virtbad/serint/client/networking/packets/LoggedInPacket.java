package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent when a client has connected successfully
 * @author Virt
 */
public class LoggedInPacket extends Packet {

    @Getter
    private String version, name, description;

    /**
     * Constructor
     * @param version version of the server
     * @param name name of the server
     * @param description description of the server
     */
    public LoggedInPacket(String version, String name, String description) {
        super(2);
        this.version = version;
        this.name = name;
        this.description = description;
    }
}
