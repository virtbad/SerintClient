package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;

/**
 * Starts a game
 */
public class GameStartPacket extends Packet {

    /**
     * Constructor
     */
    public GameStartPacket() {
        super(51);
    }
}
