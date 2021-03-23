package ch.virtbad.serint.client.game.map.data.requirements;

import ch.virtbad.serint.client.game.map.data.Tile;
import lombok.Getter;

/**
 * This class represents a requirement that an aspect requires to be shown
 * This specific requirement is bound onto the type
 */
@Getter
public class AspectTypeRequirement extends AspectRequirement {

    private Tile.Type targetType;

    @Override
    public boolean matches(int id, Tile.Type type) {
        return isInverted() == (type != targetType);
    }
}
