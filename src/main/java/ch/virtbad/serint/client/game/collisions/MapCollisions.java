package ch.virtbad.serint.client.game.collisions;

import ch.virtbad.serint.client.game.map.TileMap;

/**
 * @author Virt
 */
public class MapCollisions {
    private static final int NONE = 0, WALL = 1, FLOOR = 2;

    private int width, height;
    private boolean[][] tiles;

    public MapCollisions(TileMap map){
        extractWallTiles(map.getTiles(), map.getWidth(), map.getHeight());
    }

    public void extractWallTiles(TileMap.Tile[] mapTiles, int width, int height){
        int[][] map = new int[width][height];
        tiles = new boolean[width][height];
        this.width = width;
        this.height = height;

        for (TileMap.Tile tile : mapTiles) {
            map[tile.getX()][tile.getY()] = FLOOR;

            if (tile.getX() != 0 && map[tile.getX() - 1][tile.getY()] == NONE) map[tile.getX() - 1][tile.getY()] = WALL;
            if (tile.getX() != width - 1 && map[tile.getX() + 1][tile.getY()] == NONE) map[tile.getX() + 1][tile.getY()] = WALL;
            if (tile.getY() != 0 && map[tile.getX()][tile.getY() - 1] == NONE) map[tile.getX()][tile.getY() - 1] = WALL;
            if (tile.getY() != height - 1 && map[tile.getX()][tile.getY() + 1] == NONE) map[tile.getX()][tile.getY() + 1] = WALL;

        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] == WALL) tiles[i][j] = true;
            }
        }
    }

    public CollisionResult checkCollisions(AABB target){
        int range = 1;

        int xStart = (int) Math.floor(target.getX()) - range;
        int xEnd = (int) Math.ceil(target.getX() + target.getHeight()) + range;
        int yStart = (int) Math.floor(target.getY()) - range;
        int yEnd = (int) Math.ceil(target.getY() + target.getHeight()) + range;

        CollisionResult result = new CollisionResult();

        for (int i = xStart; i <= xEnd; i++) {
            if (i < 0 || i >= width) continue;

            for (int j = yStart; j <= yEnd; j++) {
                if (j < 0 || j >= height) continue;

                if (tiles[i][j]) { // I and J represent coordinates

                    float xStartPadding = -1;
                    float xEndPadding = -1;
                    float yStartPadding = -1;
                    float yEndPadding = -1;

                    if (target.getXEnd() <= i){ // In front of Tile
                        if (target.getYEnd() <= j) { // Below
                            // Nothing
                        } else if (target.getYStart() >= (j + 1)){ // On top
                            // Nothing
                        } else { // Would collide on the y axis
                            xEndPadding = i - target.getXEnd();
                        }

                    } else if (target.getXStart() >= (i + 1)){ // Behind the Tile

                        if (target.getYEnd() <= j) { // Below
                            // Nothing
                        } else if (target.getYStart() >= (j + 1)){ // On top
                            // Nothing
                        } else { // Would collide on the y axis
                            xStartPadding = target.getXStart() - (i + 1);
                        }

                    } else { // Would collide on the x axis

                        if (target.getYEnd() <= j) { // Below
                            yEndPadding = j - target.getYEnd();
                        } else if (target.getYStart() >= (j + 1)){ // On top
                            yStartPadding = target.getYStart() - (j + 1);
                        } else { // Would collide on the y axis
                            // Go Reverse
                            // This code kicks out the player on the x-axis

                            float xStartDelta = target.getXStart() - (i + 1);
                            float xEndDelta = i - target.getXEnd();

                            xStartPadding = xStartDelta;
                            xEndPadding = xEndDelta;
                        }

                    }

                    if (xEndPadding != -1 && (xEndPadding < result.getXEndPadding() || result.getXEndPadding() == -1)) result.setXEndPadding(xEndPadding);
                    if (xStartPadding != -1 && (xStartPadding < result.getXStartPadding() || result.getXStartPadding() == -1)) result.setXStartPadding(xStartPadding);
                    if (yEndPadding != -1 && (yEndPadding < result.getYEndPadding() || result.getYEndPadding() == -1)) result.setYEndPadding(yEndPadding);
                    if (yStartPadding != -1 && (yStartPadding< result.getYStartPadding() || result.getYStartPadding() == -1)) result.setYStartPadding(yStartPadding);
                }
            }
        }

        return result;
    }


}
