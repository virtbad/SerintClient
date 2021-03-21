package ch.virtbad.serint.client.game.map;


import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class SheetLoader {

    public static final String DEFAULT_PATH = "/assets/sheets/wallsheet.json";

    @Getter
    private WallSheet[] defaultSheets;

    public void load() {
        log.info("Loading WallSheet from {}", DEFAULT_PATH);
        InputStream stream = ResourceHelper.getClasspathResource(DEFAULT_PATH);
        if(stream == null) log.error("Failed to load Wallsheet {}", DEFAULT_PATH);
        else defaultSheets = new Gson().fromJson(new InputStreamReader(stream), WallSheet[].class);
    }
}
