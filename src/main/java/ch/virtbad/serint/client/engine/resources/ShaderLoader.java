package ch.virtbad.serint.client.engine.resources;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This class loads and stores shaders for later use
 * @author Virt
 */
@Slf4j
public class ShaderLoader {

    private final String path;
    private ResourceIndicator indicator;

    private Shader defaultShader;
    private final HashMap<String, Shader> map;

    /**
     * Initializes the ShaderLoader
     * @param path path to search for shaders by default
     */
    public ShaderLoader(String path){
        this.path = path;

        map = new HashMap<>();
    }

    /**
     * Loads all shaders from the default path
     */
    public void load(){
        log.info("Starting to load Shaders");

        InputStream indStream = ResourceHelper.getClasspathResource(path + ResourceIndicator.DEFAULT_NAME);
        if (indStream == null) throw new IllegalStateException("Cannot load Shaders from missing indicator file!");
        indicator = new Gson().fromJson(new InputStreamReader(indStream), ResourceIndicator.class);

        loadDefault(path + indicator.forgeFilename(indicator.getDefaultResource()));

        for (String resource : indicator.getResources()) {
            loadShader(path + indicator.forgeFilename(resource), resource);
        }
    }

    /**
     * Loads a shader with the given filename and a given name
     * @param file filename of the shader
     * @param name name of the shader
     */
    private void loadShader(String file, String name){
        log.info("Loading Shader: " + file);
        String source = ResourceHelper.getText(ResourceHelper.getClasspathResource(file));

        if (source == null){
            log.warn("Failed to load Shader: " + file);
            return;
        }

        try {
            Shader shader = new Shader(source);

            map.put(name, shader);
        }catch (RuntimeException e){
            // Do not crash if shader syntax error or things like that
            log.warn("Failed to load Shader: " + file);
            e.printStackTrace();
        }
    }

    /**
     * Loads the default Shader
     * @param path Path to load the default shader from
     */
    private void loadDefault(String path){
        log.info("Loading default Shader");
        String source = ResourceHelper.getText(ResourceHelper.getClasspathResource(path));
        if (source == null) throw new RuntimeException("Failed to load default Shader");
        defaultShader = new Shader(source);
    }

    /**
     * Returns the Shader associated with that name
     * @param name name of the shader
     * @return Shader, default one if failed
     */
    public Shader get(String name){
        Shader loaded = map.get(name);

        return loaded == null ? defaultShader : loaded;
    }
}

