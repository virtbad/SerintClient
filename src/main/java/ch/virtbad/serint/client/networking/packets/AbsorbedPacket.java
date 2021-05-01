package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import lombok.Getter;

/**
 * This packet is sent to a player when he is absorbed
 */
public class AbsorbedPacket extends Packet {

    @Getter
    private String absorberName;
    @Getter
    private float respawnTime;


    /**
     * Constructor
     * @param absorberName name of the absorber
     * @param respawnTime time until respawn
     */
    public AbsorbedPacket(String absorberName, float respawnTime) {
        super(52);
        this.absorberName = absorberName;
        this.respawnTime = respawnTime;
    }
}
