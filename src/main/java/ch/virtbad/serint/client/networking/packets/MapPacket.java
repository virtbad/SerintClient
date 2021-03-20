package ch.virtbad.serint.client.networking.packets;

import ch.virt.pseudopackets.packets.Packet;
import ch.virtbad.serint.client.game.map.TileMap;
import lombok.Getter;

/**
 * Sent when the server transmits a new map over to the client
 */
@Getter
public class MapPacket extends Packet {

    private String name;
    private TileMap map;

    /**
     * Constructor
     *
     * @param map  map instance
     * @param name name of the map e.g. "Lobby"
     */
    public MapPacket(String name, TileMap map) {
        super(20);
        this.name = name;
        this.map = map;
    }
}
