package ch.virtbad.serint.client.game.map;

import java.util.ArrayList;

/**
 * @author Virt
 */
public class MapOperations {
    public static final int TILE_HEIGHT = 16;
    public static final int TILE_WIDTH = 16;
    public static final int[] WALLTOP =                     new int[]{ 0, 0};
    public static final int[] WALLTOP_RIGHT =               new int[]{ 1, 0};
    public static final int[] WALLTOP_TOP =                 new int[]{ 2, 0};
    public static final int[] WALLTOP_BOTTOM =              new int[]{ 3, 0};
    public static final int[] WALLTOP_LEFT =                new int[]{ 4, 0};
    public static final int[] WALLTOP_RIGHT_TOP =           new int[]{ 5, 0};
    public static final int[] WALLTOP_RIGHT_BOTTOM =        new int[]{ 6, 0};
    public static final int[] WALLTOP_RIGHT_LEFT =          new int[]{ 7, 0};
    public static final int[] WALLTOP_TOP_BOTTOM =          new int[]{ 8, 0};
    public static final int[] WALLTOP_TOP_LEFT =            new int[]{ 9, 0};
    public static final int[] WALLTOP_BOTTOM_LEFT =         new int[]{10, 0};
    public static final int[] WALLTOP_RIGHT_TOP_BOTTOM =    new int[]{ 5, 0};
    public static final int[] WALLTOP_RIGHT_TOP_LEFT =      new int[]{ 5, 0};
    public static final int[] WALLTOP_RIGHT_BOTTOM_LEFT =   new int[]{ 6, 0};
    public static final int[] WALLTOP_TOP_BOTTOM_LEFT =     new int[]{ 8, 0};
    public static final int[] WALLTOP_TOP_RIGHT_LEFT_BOTTOM = new int[]{0,0};
    public static final int[] WALL =                        new int[]{11, 0};
    public static final int[] FLOOR_STONEBRICK =            new int[]{12, 0};

    public static final float TILESHEET_WIDTH = 16f;
    public static final float TILESHEET_HEIGHT = 16f;

    /**
     * Creates a map of rendered tiles for the base tilemap
     * @param mapTiles tiles of the map
     * @param width width of the tiles
     * @param height height of the tiles
     * @return map of rendered tiles
     */
    public static RenderedTile[] createRenderMap(TileMap.Tile[] mapTiles, int width, int height){
        RenderedTile[] tiles = new RenderedTile[width * height];
        for (int i = 0; i < tiles.length; i++) tiles[i] = new RenderedTile(WALLTOP[0], WALLTOP[1], i);
        for (TileMap.Tile tile : mapTiles) {
            int x = tile.getX();
            int y = tile.getY();
            TileMap.Tile.TileType type = tile.getType();
            int index = y * width + x;
            tiles[index] = new RenderedTile(FLOOR_STONEBRICK[0], FLOOR_STONEBRICK[1], index);
        }

        for (int i = 0; i < tiles.length ; i++) {
            RenderedTile tile = tiles[i];

            if(tile.getTextureX() != WALLTOP[0] && tile.getTextureY() != WALLTOP[1]) continue;

            // THIS IS DEFINITELY (NOT) THE BEST PRACTICE;

            RenderedTile topTile = null;
            RenderedTile leftTile = null;
            RenderedTile rightTile = null;
            RenderedTile bottomTile = null;

            boolean top = false;
            boolean left = false;
            boolean right = false;
            boolean bottom = false;

            if(tile.getIndex() - width >= 0) topTile = tiles[tile.getIndex() -width];
            if(tile.getIndex()-1 >= 0) leftTile = tiles[tile.getIndex()-1];
            if(tile.getIndex() + 1 < width * height) rightTile = tiles[tile.getIndex() + 1];
            if(tile.getIndex() + width < width * height ) bottomTile = tiles[tile.getIndex() + width];

            if(topTile != null && topTile.getTextureX() != WALLTOP[0]&& topTile.getTextureY() != WALLTOP[1]) top = true;
            if(leftTile != null && leftTile.getTextureX() != WALLTOP[0]&& leftTile.getTextureY() != WALLTOP[1]) left = true;
            if(rightTile != null && rightTile.getTextureX() != WALLTOP[0]&& rightTile.getTextureY() != WALLTOP[1]) right = true;
            if(bottomTile != null && bottomTile.getTextureX() != WALLTOP[0]&& bottomTile.getTextureY() != WALLTOP[1]) bottom = true;

            if(top && left && right && bottom) tiles[i] = new RenderedTile(WALLTOP_TOP_RIGHT_LEFT_BOTTOM[0], WALLTOP_TOP_RIGHT_LEFT_BOTTOM[1], i);
            else if(top && left && right) tiles[i] = new RenderedTile(WALLTOP_RIGHT_TOP_LEFT[0], WALLTOP_RIGHT_TOP_LEFT[1], i);
            else if(left && right && bottom) tiles[i] = new RenderedTile(WALLTOP_RIGHT_BOTTOM_LEFT[0], WALLTOP_RIGHT_BOTTOM_LEFT[1], i);
            else if(top && left && bottom) tiles[i] = new RenderedTile(WALLTOP_TOP_BOTTOM_LEFT[0], WALLTOP_TOP_BOTTOM_LEFT[1], i);
            else if(left && bottom) tiles[i] = new RenderedTile(WALLTOP_BOTTOM_LEFT[0], WALLTOP_BOTTOM_LEFT[1], i);
            else if(left && top) tiles[i] = new RenderedTile(WALLTOP_TOP_LEFT[0], WALLTOP_TOP_LEFT[1], i);
            else if(right && left) tiles[i] = new RenderedTile(WALLTOP_RIGHT_LEFT[0], WALLTOP_RIGHT_LEFT[1], i);
            else if(top && bottom) tiles[i] = new RenderedTile(WALLTOP_TOP_BOTTOM[0], WALLTOP_TOP_BOTTOM[1], i);
            else if(right && bottom) tiles[i] = new RenderedTile(WALLTOP_RIGHT_BOTTOM[0], WALLTOP_RIGHT_BOTTOM[1], i);
            else if(top && right) tiles[i] = new RenderedTile(WALLTOP_RIGHT_TOP[0], WALLTOP_RIGHT_TOP[1], i);
            else if(left) tiles[i] = new RenderedTile( WALLTOP_LEFT[0], WALLTOP_LEFT[1], i);
            else if(bottom) tiles[i] = new RenderedTile(WALLTOP_BOTTOM[0], WALLTOP_BOTTOM[1], i);
            else if(right) tiles[i] = new RenderedTile(WALLTOP_RIGHT[0], WALLTOP_RIGHT[1], i);
            else if(top) tiles[i] = new RenderedTile(WALLTOP_TOP[0], WALLTOP_TOP[1], i);

            if(topTile != null && !top && bottom) tiles[i] = new RenderedTile(WALL[0], WALL[1], i);
        }

        return tiles;
    }

    /**
     * Generates vertices for a map object
     *
     * Each vertex consists of four floats
     * * Two for Position (X,Y)
     * * Two for Texture (U,V)
     *
     * There are also four vertices per tile
     *
     * @param tiles tiles to use
     * @param width width of the map
     * @param height height of the map
     * @return vertices
     */
    public static float[] generateVertices(RenderedTile[] tiles, int width, int height){

        float[] vertices = new float[width * height * 4 * 4]; // 4*4 for four attributes

        int index = 0;
        for (RenderedTile tile : tiles) {

            // Bottom Left
            vertices[index        ] = tile.getIndex() % width;
            vertices[index     + 1] = tile.getIndex() / width;

            vertices[index     + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX());
            vertices[index     + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY() + 1);

            // Bottom Right
            vertices[index + 4    ] = tile.getIndex() % width + 1;
            vertices[index + 4 + 1] = tile.getIndex() / width;

            vertices[index + 4 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX() + 1);
            vertices[index + 4 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY() + 1);

            // Top Left
            vertices[index + 8    ] = tile.getIndex() % width;
            vertices[index + 8 + 1] = tile.getIndex() / width + 1;

            vertices[index + 8 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX());
            vertices[index + 8 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY());

            // Top Right
            vertices[index + 12    ] = tile.getIndex() % width + 1;
            vertices[index + 12 + 1] = tile.getIndex() / width + 1;

            vertices[index + 12 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX() + 1);
            vertices[index + 12 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY());

            index += 4 * 4;
        }

        return vertices;
    }

    /**
     * Generates indices for Rendered tiles
     *
     * The Quad is divided like follows:
     *
     *  2-----3
     *  |   / |
     *  | /   |
     *  0-----1
     *
     * This results in the indices:
     * 0, 3, 1 and 0, 3, 2
     *
     * @param tiles tiles
     * @param width width of the map
     * @param height height of the map
     * @return indices
     */
    public static int[] generateIndices(RenderedTile[] tiles, int width, int height){
        int[] indices = new int[width * height * 2 * 3]; // 2*3 for two triangles per tile

        int index = 0;
        int cornerIndex = 0;
        for (RenderedTile ignored : tiles) {

            indices[index        ] = cornerIndex;
            indices[index     + 1] = cornerIndex + 3;
            indices[index     + 2] = cornerIndex + 1;

            indices[index + 3    ] = cornerIndex;
            indices[index + 3 + 1] = cornerIndex + 3;
            indices[index + 3 + 2] = cornerIndex + 2;

            index += 2 * 3;
            cornerIndex += 4;
        }

        return indices;
    }
}
