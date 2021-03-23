package ch.virtbad.serint.client.game.map.data;

import lombok.Getter;

@Getter
public class AspectRequirement {
    public static enum Type {
        TILE,
        TYPE
    }

    private Type type;
    private boolean inverted;

    public boolean matches(int id, Tile.Type type){
        return true;
    }
}
