package ch.virtbad.serint.client.game.map.data;

import lombok.Getter;

@Getter
public class AspectTileRequirement extends AspectRequirement {

    private int targetTile;

    @Override
    public boolean matches(int id, Tile.Type type) {
        return isInverted() == (id != targetTile);
    }
}
