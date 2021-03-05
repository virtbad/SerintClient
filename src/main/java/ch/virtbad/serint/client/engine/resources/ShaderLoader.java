package ch.virtbad.serint.client.engine.resources;

import java.util.HashMap;

/**
 * This class loads and stores shaders for later use
 * @author Virt
 */
public class ShaderLoader {
    public static final String FILE_ENDING = ".glsl";
    public static final String FILE_DEFAULT = "default.glsl";

    private final String path;

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
        loadDefault();
        load(path);
    }

    /**
     * Loads all Shaders from a given classpath directory
     * @param folder directory to load from
     */
    public void load(String folder){
        System.out.println("Loading Shader Directory: " + folder);
        String content = ResourceHelper.getText(ResourceHelper.getClasspathResource(folder));
        if (content == null) throw new IllegalStateException("Failed to load shaders from folder: " + folder);

        String[] files = content.split("\n");
        for (String file : files) {
            file = file.trim();

            if (file.endsWith(FILE_ENDING)) loadShader(folder + "/" + file, file.substring(0, file.length() - FILE_ENDING.length()));
        }
    }

    /**
     * Loads a shader with the given filename and a given name
     * @param file filename of the shader
     * @param name name of the shader
     */
    private void loadShader(String file, String name){
        System.out.println("Loading Shader: " + file);
        String source = ResourceHelper.getText(ResourceHelper.getClasspathResource(file));

        if (source == null){
            System.err.println("Failed to load Shader: " + file);
            return;
        }

        try {
            Shader shader = new Shader(source);

            map.put(name, shader);
        }catch (RuntimeException e){
            // Do not crash if shader syntax error or things like that
            System.out.println("Failed to load Shader: " + file);
            e.printStackTrace();
        }
    }

    /**
     * Loads the default Shader
     */
    private void loadDefault(){
        System.out.println("Loading default Shader");
        String source = ResourceHelper.getText(ResourceHelper.getClasspathResource(path + "/" + FILE_DEFAULT));

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

