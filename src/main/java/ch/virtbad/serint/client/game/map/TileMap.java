package ch.virtbad.serint.client.game.map;

import lombok.Getter;

/**
 * This class stores basic map information
 */
@Getter
public class TileMap {

    private int height;
    private int width;
    private String name;

    private Tile[] tiles;
    private LightSource[] lights;
    private Cosmetic[] cosmetics;

    /**
     * Creates a map with all of its components.
     * (and yes i do not describe the parameters)
     */
    private TileMap(int height, int width, String name, Tile[] tiles, LightSource[] lights,  Cosmetic[] cosmetics) {
        this.height = height;
        this.width = width;
        this.name = name;
        this.tiles = tiles;
        this.lights = lights;
        this.cosmetics = cosmetics;
    }

    @Getter
    public static class Tile {
        private int x;
        private int y;
        private int type;
    }

    @Getter
    public static class LightSource {
        private float x;
        private float y;
        private float intensity;
        private RGB color;

        @Getter
        public static class RGB {
            private int r;
            private int g;
            private int b;
        }
    }

    @Getter
    public static class Cosmetic {
        private int x;
        private int y;
        private CosmeticType type;

        public enum CosmeticType {
            COBWEB,
            CRACK
        }
    }
}
