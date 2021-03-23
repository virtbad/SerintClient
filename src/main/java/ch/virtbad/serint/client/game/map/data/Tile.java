package ch.virtbad.serint.client.game.map.data;

import lombok.Getter;

/**
 * This class represents a tile on the map
 */
@Getter
public class Tile {

    /**
     * This enum states the type of a tile:
     * TOP: The tile is a top of a wall, it cannot be walked on
     * WALL: The tile represents like a wall of a dungeon
     * FLOOR; The tile represents a floor and can be walked on
     */
    public static enum Type {
        TOP,
        FLOOR,
        WALL
    }

    private int id;
    private TextureLocation texture;
    private Type type;

}
