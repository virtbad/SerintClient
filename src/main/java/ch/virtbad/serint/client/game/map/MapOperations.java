package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.game.map.data.Aspect;
import ch.virtbad.serint.client.game.map.data.TextureLocation;
import ch.virtbad.serint.client.game.map.data.Tile;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author Virt
 */
@Slf4j
public class MapOperations {
    public static final int[] WALL = new int[]{4, 1};
    public static final int[] FLOOR_STONEBRICK = new int[]{5, 1};

    public static final float TILESHEET_WIDTH = 16f;
    public static final float TILESHEET_HEIGHT = 16f;

    public static TextureLocation[][][] createRenderMap(TileMap.Tile[] mapTiles, int width, int height) {
        int top = 5, wall = 4, none = 0;
        int[][] tiles = new int[width][height];

        for (int[] ints : tiles) Arrays.fill(ints, top);

        for (TileMap.Tile tile : mapTiles) {
            tiles[tile.getX()][tile.getY()] = tile.getType();
            if (tile.getY() < width - 1 && tiles[tile.getX()][tile.getY() + 1] == top)
                tiles[tile.getX()][tile.getY() + 1] = wall;
        }

        TextureLocation[][][] rendered = new TextureLocation[width][height][9]; // one ground, 8 aspect textures

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int current = tiles[i][j];

                // Query Grounds
                for (Tile tile : ResourceHandler.getData().getTiles()) {
                    if (tile.getId() == current){
                        rendered[i][j][0] = tile.getTexture();
                        break;
                    }
                }

                if (rendered[i][j][0] == null) {
                    rendered[i][j][0] = new TextureLocation(0, 0);
                    log.warn("Tiletype {} at {},{} was not found", tiles[i][j], i, j);
                }

                // Query Aspects
                Aspect[] aspects = ResourceHandler.getData().getAspects();
                int topTile = i - 1 >= 0 ? tiles[i - 1][j] : -1;
                int rightTile = j + 1 <= width ? tiles[i][j + 1] : -1;
                int bottomTile = i + 1 <= height ? tiles[i + 1][j] : -1;
                int leftTile = j - 1 >= 0 ? tiles[i][j - 1] : -1;
                int bottomRightTile = bottomTile != -1 && rightTile != -1 ? tiles[i + 1][j + 1] : -1;
                int bottomLeftTile = bottomTile != -1 && leftTile != -1 ? tiles[i + 1][j - 1] : -1;
                int topRightTile = topTile != -1 && rightTile != -1 ? tiles[i - 1][j + 1] : -1;
                int topLeftTile = topTile != -1 && leftTile != -1 ? tiles[i - 1][j - 1] : -1;

                int firstIndex = 1, secondIndex = 5;

                for (Aspect aspect : aspects) {
                    if (aspect.getTarget() != current) continue;

                    // Quick and dirty from here [buy now for good price]:
                    if (matchesAll(aspect, topTile, rightTile, bottomTile, leftTile, topLeftTile, topRightTile, bottomLeftTile, bottomRightTile)){
                        if (aspect.getLayer() == 1) { // First layer
                            if (firstIndex > 4){
                                log.warn("Reached 1st Level aspect limit with tile {} at {},{}", tiles[i][j], i, j);
                                continue;
                            }

                            rendered[i][j][firstIndex] = aspect.getTexture();

                            firstIndex++;
                        }else if (aspect.getLayer() == 2){
                            if (secondIndex == 9) {
                                log.warn("Reached 1st Level aspect limit with tile {} at {},{}", tiles[i][j], i, j);
                                continue;
                            }

                            rendered[i][j][secondIndex] = aspect.getTexture();

                            secondIndex++;
                        }
                    }
                }
            }
        }

        return rendered;
    }

    /**
     * Returns the type of a tile
     * @param id tile to check
     * @return type of tile
     */
    private static Tile.Type getType(int id) {
        for (Tile tile : ResourceHandler.getData().getTiles()) {
            if (tile.getId() == id) return tile.getType();
        }

        return Tile.Type.TOP; // Default option
    }

    /**
     * Returns whether the aspect matches all sides
     * @param aspect aspect to check
     * [The other parameters are the ids of the tiles at those positions]
     * @return whether the aspects are true
     */
    private static boolean matchesAll(Aspect aspect, int top, int right, int bottom, int left, int topLeft, int topRight, int bottomLeft, int bottomRight) {
        if (aspect.getTop() != null && !aspect.getTop().matches(top, getType(top))) return false;
        if (aspect.getBottom() != null && !aspect.getBottom().matches(bottom, getType(bottom))) return false;
        if (aspect.getRight() != null && !aspect.getRight().matches(right, getType(right))) return false;
        if (aspect.getLeft() != null && !aspect.getLeft().matches(left, getType(left))) return false;
        if (aspect.getTopLeft() != null && !aspect.getTopLeft().matches(topLeft, getType(topLeft))) return false;
        if (aspect.getTopRight() != null && !aspect.getTopRight().matches(topRight, getType(topRight))) return false;
        if (aspect.getBottomLeft() != null && !aspect.getBottomLeft().matches(bottomLeft, getType(bottomLeft))) return false;
        if (aspect.getBottomRight() != null && !aspect.getBottomRight().matches(bottomRight, getType(bottomRight))) return false;

        return true;
    }

    /**
     * Generates vertices for a map object
     * <p>
     * Each vertex consists of four floats
     * * Two for Position (X,Y)
     * * Two for Texture (U,V)
     * <p>
     * There are also four vertices per tile
     *
     * @param tiles  tiles to use
     * @param width  width of the map
     * @param height height of the map
     * @return vertices
     */
    public static float[] generateVertices(RenderedTile[] tiles, int width, int height) {

        float[] vertices = new float[width * height * 4 * (2 + 9 * 2)]; // 4*20 for 20 attributes for four vertices

        int index = 0;
        for (RenderedTile tile : tiles) {

            // Bottom Left
            vertices[index] = tile.getIndex() % width;
            vertices[index + 1] = tile.getIndex() / width;

            vertices[index + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX());
            vertices[index + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY() + 1);

            // Bottom Right
            vertices[index + 4] = tile.getIndex() % width + 1;
            vertices[index + 4 + 1] = tile.getIndex() / width;

            vertices[index + 4 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX() + 1);
            vertices[index + 4 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY() + 1);

            // Top Left
            vertices[index + 8] = tile.getIndex() % width;
            vertices[index + 8 + 1] = tile.getIndex() / width + 1;

            vertices[index + 8 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX());
            vertices[index + 8 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY());

            // Top Right
            vertices[index + 12] = tile.getIndex() % width + 1;
            vertices[index + 12 + 1] = tile.getIndex() / width + 1;

            vertices[index + 12 + 2] = 1 / TILESHEET_WIDTH * (tile.getTextureX() + 1);
            vertices[index + 12 + 3] = 1 / TILESHEET_HEIGHT * (tile.getTextureY());

            index += 4 * 4;
        }

        return vertices;
    }

    /**
     * Generates indices for Rendered tiles
     * <p>
     * The Quad is divided like follows:
     * <p>
     * 2-----3
     * |   / |
     * | /   |
     * 0-----1
     * <p>
     * This results in the indices:
     * 0, 3, 1 and 0, 3, 2
     *
     * @param tiles  tiles
     * @param width  width of the map
     * @param height height of the map
     * @return indices
     */
    public static int[] generateIndices(RenderedTile[] tiles, int width, int height) {
        int[] indices = new int[width * height * 2 * 3]; // 2*3 for two triangles per tile

        int index = 0;
        int cornerIndex = 0;
        for (RenderedTile ignored : tiles) {

            indices[index] = cornerIndex;
            indices[index + 1] = cornerIndex + 3;
            indices[index + 2] = cornerIndex + 1;

            indices[index + 3] = cornerIndex;
            indices[index + 3 + 1] = cornerIndex + 3;
            indices[index + 3 + 2] = cornerIndex + 2;

            index += 2 * 3;
            cornerIndex += 4;
        }

        return indices;
    }
}
