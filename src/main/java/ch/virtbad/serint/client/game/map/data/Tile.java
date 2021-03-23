package ch.virtbad.serint.client.game.map.data;

import lombok.Getter;

@Getter
public class Tile {

    public static enum Type {
        TOP,
        FLOOR,
        WALL
    }

    private int id;
    private TextureLocation texture;
    private Type type;

}
