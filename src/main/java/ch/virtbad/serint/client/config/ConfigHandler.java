package ch.virtbad.serint.client.config;

import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * This class handles loading the config
 * @author Virt
 */
@Slf4j
public class ConfigHandler {
    public static final String PATH = "config.json";

    private static Config config;

    /**
     * Creates a ConfigHandler.
     * (basically just loads it with a default path or a custom one)
     * @param customPath custom path to load from, may be null
     */
    public ConfigHandler(String customPath) {
        load(customPath == null ? PATH : customPath);
    }

    /**
     * Loads the config
     * @param path path of the config to load
     */
    public static void load(String path){
        log.info("Loading Config from {}", path);

        InputStream file = ResourceHelper.getFilesystemResource(path);
        if (file == null) {
            log.warn("Failed to load Config file");
            config = new Config();
        }
        else config = new Gson().fromJson(new InputStreamReader(file), Config.class);
    }

    public static void save(){
        log.info("Saving config to default path");
        try {
            FileWriter writer = new FileWriter(PATH);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a instance of the config to access
     * @return config
     */
    public static Config getConfig(){
        return config;
    }
}
