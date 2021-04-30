package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.game.map.data.Aspect;
import ch.virtbad.serint.client.game.map.data.TextureLocation;
import ch.virtbad.serint.client.game.map.data.Tile;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author Virt
 * This class is for handling or calculating various things regarding the map
 */
@Slf4j
public class MapOperations {

    public static final int TEXTURE_LAYERS = 9;

    /**
     * This method is used for creating an array of texture locations that are
     * @param mapTiles tiles from the map data
     * @param width width of the map
     * @param height height of the map
     * @return 3 dimensional array containing a bunch of layers for each tile
     */
    public static TextureLocation[][][] createRenderMap(TileMap.Tile[] mapTiles, int width, int height) {
        int top = 5, wall = 4, none = 0; // TODO: Include default walls and tiles in map data
        int[][] tiles = new int[width][height];

        for (int[] ints : tiles) Arrays.fill(ints, top);

        for (TileMap.Tile tile : mapTiles) {
            tiles[tile.getX()][tile.getY()] = tile.getType();
            if (tile.getY() < width - 1 && tiles[tile.getX()][tile.getY() + 1] == top)
                tiles[tile.getX()][tile.getY() + 1] = wall;
        }

        TextureLocation[][][] rendered = new TextureLocation[width][height][TEXTURE_LAYERS]; // one ground, 8 aspect textures

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
                    log.warn("Tile type {} at {},{} was not found", tiles[i][j], i, j);
                }

                // Query Aspects
                Aspect[] aspects = ResourceHandler.getData().getAspects();
                int topTile = j + 1 < height ? tiles[i][j + 1] : -1;
                int rightTile = i + 1 < width ? tiles[i + 1][j] : -1;
                int bottomTile = j - 1 >= 0 ? tiles[i][j - 1] : -1;
                int leftTile = i - 1 >= 0 ? tiles[i - 1][j] : -1;
                int bottomRightTile = bottomTile != -1 && rightTile != -1 ? tiles[i + 1][j - 1] : -1;
                int bottomLeftTile = bottomTile != -1 && leftTile != -1 ? tiles[i - 1][j - 1] : -1;
                int topRightTile = topTile != -1 && rightTile != -1 ? tiles[i + 1][j + 1] : -1;
                int topLeftTile = topTile != -1 && leftTile != -1 ? tiles[i - 1][j + 1] : -1;

                int firstIndex = 1, secondIndex = 5;

                for (Aspect aspect : aspects) {
                    if (aspect.getTarget() != current) continue;

                    // Quick and dirty from here:
                    if (matchesAll(aspect, topTile, rightTile, bottomTile, leftTile, topLeftTile, topRightTile, bottomLeftTile, bottomRightTile)){
                        if (aspect.getLayer() == 1) { // First layer
                            if (firstIndex > (TEXTURE_LAYERS - 1) / 2){
                                log.warn("Reached 1st Level aspect limit with tile {} at {},{}", tiles[i][j], i, j);
                                continue;
                            }

                            rendered[i][j][firstIndex] = aspect.getTexture();

                            firstIndex++;
                        }else if (aspect.getLayer() == 2){
                            if (secondIndex == TEXTURE_LAYERS) {
                                log.warn("Reached 2nd Level aspect limit with tile {} at {},{}", tiles[i][j], i, j);
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
     * * 18 for Texture (U,V)
     * <p>
     *
     * A vertex will then look like the following:
     * x, y, u1, v1, u2, v2 ... uN, vN
     * If a pair of uv coordinates is not existent, they will be negative one
     *
     * There are also four vertices per tile
     *
     * @param tiles  tiles to use
     * @param width  width of the map
     * @param height height of the map
     * @return vertices
     */
    public static float[] generateVertices(TextureLocation[][][] tiles, int width, int height) {

        float[] vertices = new float[width * height * 4 * (2 + 9 * 2)]; // 4*20 for 20 attributes for four vertices

        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                // Bottom Left
                vertices[index++] = i;
                vertices[index++] = j;

                for (int k = 0; k < TEXTURE_LAYERS; k++) {
                    if (tiles[i][j][k] == null) {
                        vertices[index++] = -1;
                        vertices[index++] = -1;
                    } else {
                        vertices[index++] = tiles[i][j][k].getX();
                        vertices[index++] = tiles[i][j][k].getY() + 1;
                    }
                }

                // Bottom Right
                vertices[index++] = i + 1;
                vertices[index++] = j;

                for (int k = 0; k < TEXTURE_LAYERS; k++) {
                    if (tiles[i][j][k] == null) {
                        vertices[index++] = -1;
                        vertices[index++] = -1;
                    } else {
                        vertices[index++] = tiles[i][j][k].getX() + 1;
                        vertices[index++] = tiles[i][j][k].getY() + 1;
                    }
                }

                // Top Left
                vertices[index++] = i;
                vertices[index++] = j + 1;

                for (int k = 0; k < TEXTURE_LAYERS; k++) {
                    if (tiles[i][j][k] == null) {
                        vertices[index++] = -1;
                        vertices[index++] = -1;
                    } else {
                        vertices[index++] = tiles[i][j][k].getX();
                        vertices[index++] = tiles[i][j][k].getY();
                    }
                }

                // Top Left
                vertices[index++] = i + 1;
                vertices[index++] = j + 1;

                for (int k = 0; k < TEXTURE_LAYERS; k++) {
                    if (tiles[i][j][k] == null) {
                        vertices[index++] = -1;
                        vertices[index++] = -1;
                    } else {
                        vertices[index++] = tiles[i][j][k].getX() + 1;
                        vertices[index++] = tiles[i][j][k].getY();
                    }
                }
            }
        }

        return vertices;
    }

    /**
     * Generates vertices for cosmetic tiles
     * <p>
     * Each vertex consists of four floats
     * * Two for Position (X,Y)
     * * Two for texture position (X,Y)
     * <p>
     *
     * There are also four vertices per tile
     *
     * @param tiles cosmetic tiles of the map
     * @return vertices
     */

    public static float[] generateCosmeticVertices(TileMap.Cosmetic[] tiles) {

        int textureWidth = 8, textureHeight = 8;

        float[] cosmetics = new float[tiles.length * 4 * 4]; // RelativeX, RelativeY, TextureX, TextureY

        for (int i = 0; i < tiles.length; i++) {
            TileMap.Cosmetic cosmetic = tiles[i];
            for (int j = 0; j < 4; j++) {
                int relativeX = j == 0 || j == 3 ? 0 : 1;
                int relativeY = j == 2 || j == 3 ? 0 : 1;

                int textureY = cosmetic.getType() / textureHeight;
                int textureX = cosmetic.getType() - textureY * textureWidth;

                int index = i * 16 + j * 4;
                cosmetics[index] = cosmetic.getX() + relativeX;
                cosmetics[index + 1] = cosmetic.getY() + relativeY;
                cosmetics[index + 2] = textureX + relativeX;
                cosmetics[index + 3] = textureY + relativeY == 1 ? 0 : 1;
            }
        }

        return cosmetics;
    }

    /**
     * Generates indices for Rendered tiles
     * <p>
     * The Quad is divided like follows:
     * <p>
     * 2-----3      <br>
     * |   / |      <br>
     * | /   |      <br>
     * 0-----1      <br>
     * <p>
     * This results in the indices:
     * 0, 3, 1 and 0, 3, 2
     *
     * @param width  width of the map
     * @param height height of the map
     * @return indices
     */
    public static int[] generateIndices(int width, int height) {
        int[] indices = new int[width * height * 2 * 3]; // 2*3 for two triangles per tile

        int index = 0;
        int cornerIndex = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                indices[index++] = cornerIndex;
                indices[index++] = cornerIndex + 3;
                indices[index++] = cornerIndex + 1;

                indices[index++] = cornerIndex;
                indices[index++] = cornerIndex + 3;
                indices[index++] = cornerIndex + 2;

                cornerIndex += 4;

            }
        }

        return indices;
    }

    /**
     * Generates indices for Rendered tiles
     * <p>
     * The Quad is divided like follows:
     * <p>
     * 2-----3      <br>
     * |   / |      <br>
     * | /   |      <br>
     * 0-----1      <br>
     * <p>
     * This results in the indices:
     * 0, 3, 1 and 0, 3, 2
     *
     * @param tiles cosmetic tiles of the map
     * @return indices
     */

    public static int[] generateCosmeticIndices(TileMap.Cosmetic[] tiles) {
        int[] indices = new int[tiles.length * 2 * 3];

        for (int i = 0; i < tiles.length; i++) {
            int index = i * 3 * 2;
            int vIndex = i * 4;
            indices[index] = vIndex;
            indices[index + 1] = vIndex + 1;
            indices[index + 2] = vIndex + 2;
            indices[index + 3] = vIndex;
            indices[index + 4] = vIndex + 3;
            indices[index + 5] = vIndex + 2;
        }

        return indices;
    }
}
