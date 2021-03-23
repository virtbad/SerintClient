package ch.virtbad.serint.client.game.map.data.requirements;

import ch.virtbad.serint.client.game.map.data.Tile;
import lombok.Getter;

/**
 * This class represents a requirement that has to be fulfilled for an aspect to be shown.
 */
@Getter
public class AspectRequirement {

    /**
     * This enum represents the type of a requirement
     */
    public static enum Type {
        TILE,
        TYPE
    }

    private Type type;
    private boolean inverted;

    /**
     * This method returns whether the requirement is fulfilled or not
     * @param id id of the tile to compare
     * @param type type of the tile to check
     * @return whether the requirement is fulfilled
     */
    public boolean matches(int id, Tile.Type type){
        return true;
    }
}
