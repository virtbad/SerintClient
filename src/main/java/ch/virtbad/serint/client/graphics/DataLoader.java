package ch.virtbad.serint.client.graphics;


import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import ch.virtbad.serint.client.game.map.data.Aspect;
import ch.virtbad.serint.client.game.map.data.Tile;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class loads special jsons into memory for specific usage
 */
@Slf4j
public class DataLoader {

    public static final String ASPECT_PATH = "/assets/data/aspects.json";
    public static final String TILE_PATH = "/assets/data/tiles.json";

    @Getter
    private Aspect[] aspects;
    @Getter
    private Tile[] tiles;

    /**
     * Loads the jsons
     */
    public void load() {
        log.info("Loading Aspects from {}", ASPECT_PATH);
        InputStream stream = ResourceHelper.getClasspathResource(ASPECT_PATH);
        if(stream == null) log.error("Failed to load Aspects");
        else aspects = new Gson().fromJson(new InputStreamReader(stream), Aspect[].class);

        log.info("Loading Tiles from {}", TILE_PATH);
        stream = ResourceHelper.getClasspathResource(TILE_PATH);
        if(stream == null) log.error("Failed to load Tiles");
        else tiles = new Gson().fromJson(new InputStreamReader(stream), Tile[].class);
    }
}