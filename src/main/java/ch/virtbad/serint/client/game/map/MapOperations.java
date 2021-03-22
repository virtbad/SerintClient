package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.graphics.ResourceHandler;

/**
 * @author Virt
 */
public class MapOperations {
    public static final int[] WALL = new int[]{4, 1};
    public static final int[] FLOOR_STONEBRICK = new int[]{5, 1};

    public static final float TILESHEET_WIDTH = 16f;
    public static final float TILESHEET_HEIGHT = 16f;

    public static RenderedTile[] createRenderMap(TileMap.Tile[] mapTiles, int width, int height){
        RenderedTile[] tiles = new RenderedTile[width * height];
        boolean[] wallfloors = new boolean[width * height];
        boolean[] floors = new boolean[width * height];
        boolean[] borders = new boolean[width * height];

        WallSheet[] sheets = ResourceHandler.getSheets().getDefaultSheets();

        for (TileMap.Tile tile : mapTiles) {
            TileMap.Tile.TileType type = tile.getType();
            int index = tile.getY() * width + tile.getX();

            tiles[index] = new RenderedTile(FLOOR_STONEBRICK[0], FLOOR_STONEBRICK[1], index); // TODO: Dynamic Types
            wallfloors[index] = true;
            floors[index] = true;
        }

        for (int i = 0; i < tiles.length; i++) {
            if(floors[i]) continue;

            boolean bottom = false;
            if(i - width >= 0) bottom = floors[i - width];

            if(bottom) {
                tiles[i] = new RenderedTile(WALL[0], WALL[1], i); // TODO: MAKE DYNAMIC PLS NOW!
                wallfloors[i] = true;
            }

        }


        for (int i = 0; i < tiles.length ; i++) {
            if(wallfloors[i]) continue;

            // THIS IS DEFINITELY A BETTER PRACTICE THAN YESTERDAY

            boolean top = false, left = false, right = false, bottom = false;

            if(i + width < height * width) top = wallfloors[i + width];
            if(i - 1 >= 0) left = i % height != 0 && wallfloors[i - 1];
            if(i + 1 < width * height) right = i % height != width-1 && wallfloors[i + 1];
            if(i - width >= 0) bottom = wallfloors[i - width];

            boolean changed = false;
            for (WallSheet sheet : sheets) {
                if (sheet.isTop() == top && sheet.isRight() == right && sheet.isLeft() == left && sheet.isBottom() == bottom) {
                    tiles[i] = new RenderedTile(sheet.getX(), sheet.getY(), i);
                    borders[i] = top || right || left || bottom;
                    changed = true;
                    break;
                }
            }

            if (!changed) tiles[i] = new RenderedTile(sheets[0].getX(), sheets[0].getY(), i); // TODO: Make dynamic
        }

        for (int i = 0; i <  tiles.length; i++) {
            if(borders[i] || wallfloors[i]) continue;

            boolean top = false, left = false, right = false, bottom = false, corner1 = false, corner2 = false, corner3 = false, corner4 = false;
            if(i + width < height * width) top = borders[i + width];
            if(i - 1 >= 0) left = borders[i - 1];
            if(i + 1 < width * height) right = borders[i + 1];
            if(i - width >= 0) bottom = borders[i - width];


            if(!top && !left && !right && !bottom) continue;

            for (WallSheet sheet : sheets) {
                if (sheet.isTop() == top && sheet.isRight() == right && sheet.isLeft() == left && sheet.isBottom() == bottom) {
                    tiles[i] = new RenderedTile(sheet.getX(), sheet.getY(), i);
                    break;
                }
            }
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
