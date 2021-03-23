package ch.virtbad.serint.client.game.map.data;

import lombok.Getter;

@Getter
public class AspectTypeRequirement extends AspectRequirement {

    private Tile.Type targetType;

    @Override
    public boolean matches(int id, Tile.Type type) {
        return isInverted() == (type != targetType);
    }
}
