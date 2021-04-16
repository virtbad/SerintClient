package ch.virtbad.serint.client.engine.resources.textures;

import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import ch.virtbad.serint.client.engine.resources.ResourceIndicator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This class stores and loads textures from resources for later use
 * @author Virt
 */
@Slf4j
public class TextureLoader {

    private final String path;
    private ResourceIndicator indicator;

    private Texture defaultTexture;
    private final HashMap<String, Texture> map;

    /**
     * Creates a TextureLoader
     * @param path path to look for textures
     */
    public TextureLoader(String path){
        this.path = path;

        map = new HashMap<>();
    }

    /**
     * Loads the textures from the default folder
     */
    public void load(){
        log.info("Starting to load Textures");

        InputStream indStream = ResourceHelper.getClasspathResource(path + ResourceIndicator.DEFAULT_NAME);
        if (indStream == null) throw new IllegalStateException("Cannot load Textures from missing indicator file!");
        indicator = new Gson().fromJson(new InputStreamReader(indStream), ResourceIndicator.class);

        loadDefault(path + indicator.forgeFilename(indicator.getDefaultResource()));

        for (String resource : indicator.getResources()) {
            loadTexture(path + indicator.forgeFilename(resource), resource);
        }
    }

    /**
     * Loads a texture with filename and name
     * @param file filename to load the texture from
     * @param name name to store it with
     */
    public void loadTexture(String file, String name){
        log.info("Loading Texture: " + file);
        BufferedImage image = ResourceHelper.getImage(ResourceHelper.getClasspathResource(file));

        if (image == null){
            log.warn("Failed to load Texture: " + file);
            return;
        }

        try {
            Texture texture = new Texture(image);

            map.put(name, texture);
        } catch (RuntimeException e){
            // Do also not crash if somehow failing to load single texture
            log.warn("Failed to load Texture: " + file);
            e.printStackTrace();
        }
    }

    /**
     * Loads the default Texture
     * @param path Path of the default texture
     */
    public void loadDefault(String path){
        log.info("Loading default Texture");
        BufferedImage image = ResourceHelper.getImage(ResourceHelper.getClasspathResource(path));

        if (image == null) throw new RuntimeException("Failed to load default Texture");

        defaultTexture = new Texture(image);
    }

    /**
     * Returns the Texture associated with that name
     * @param name name of the texture
     * @return Texture, default if none present
     */
    public Texture get(String name){
        Texture texture = map.get(name);

        return texture == null ? defaultTexture : texture;
    }
}
