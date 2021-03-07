package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * Sent when someone gets kicked
 * @author Virt
 */
public class KickPaket extends Packet {

    @Getter
    private String reason;

    /**
     * Constructor
     * @param reason reason for kick
     */
    public KickPaket(String reason) {
        super(3);
        this.reason = reason;
    }
}
