package ch.virtbad.serint.client.game.map.data.requirements;

import ch.virtbad.serint.client.game.map.data.Tile;
import lombok.Getter;

/**
 * This class represents a requirement that an aspect requires to be shown
 * This specific requirement is bound onto the id of the tile
 */
@Getter
public class AspectTileRequirement extends AspectRequirement {

    private int targetTile;

    @Override
    public boolean matches(int id, Tile.Type type) {
        return isInverted() == (id != targetTile);
    }
}
