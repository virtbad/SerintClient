package ch.virtbad.serint.client.engine.resources;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * This class stores and loads textures from resources for later use
 * @author Virt
 */
public class TextureLoader {
    public static final String FILE_ENDING = ".png";
    public static final String FILE_DEFAULT = "default.png";

    private final String path;

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
        loadDefault();
        load(path);
    }

    /**
     * Loads textures from the folder provided
     * @param folder folder to load from
     */
    public void load(String folder){
        System.out.println("Loading Texture Directory: " + folder);
        String content = ResourceHelper.getText(ResourceHelper.getClasspathResource(folder));
        if (content == null) throw new IllegalStateException("Failed to load textures from folder: " + folder);

        String[] files = content.split("\n");
        for (String file : files) {
            file = file.trim();

            if (file.endsWith(FILE_ENDING)) loadTexture(folder + "/" + file, file.substring(0, file.length() - FILE_ENDING.length()));
        }
    }

    /**
     * Loads a texture with filename and name
     * @param file filename to load the texture from
     * @param name name to store it with
     */
    private void loadTexture(String file, String name){
        System.out.println("Loading Texture: " + file);
        BufferedImage image = ResourceHelper.getImage(ResourceHelper.getClasspathResource(file));

        if (image == null){
            System.out.println("Failed to load Texture: " + file);
            return;
        }

        try {
            Texture texture = new Texture(image);

            map.put(name, texture);
        } catch (RuntimeException e){
            // Do also not crash if somehow failing to load single texture
            System.out.println("Failed to load Texture: " + file);
            e.printStackTrace();
        }
    }

    /**
     * Loads the default Texture
     */
    private void loadDefault(){
        System.out.println("Loading default Texture");
        BufferedImage image = ResourceHelper.getImage(ResourceHelper.getClasspathResource(path + "/" + FILE_DEFAULT));

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
