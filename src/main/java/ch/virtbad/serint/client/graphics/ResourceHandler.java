package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.engine.resources.shaders.ShaderLoader;
import ch.virtbad.serint.client.engine.resources.textures.TextureLoader;
import ch.virtbad.serint.client.ui.lang.LanguageLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Virt
 */
@Slf4j
public class ResourceHandler {
    public static final String TEXTURE_LOCATION = "/assets/textures/";
    public static final String SHADER_LOCATION = "/assets/shaders/";
    public static final String LANGUAGE_LOCATION = "/assets/languages/";

    @Getter
    private static TextureLoader textures;
    @Getter
    private static ShaderLoader shaders;
    @Getter
    private static DataLoader data;
    @Getter
    private static LanguageLoader languages;

    /**
     * Initializes the Resource Loader
     */
    public static void init(){
        log.info("Initializing Resource Handler");
        textures = new TextureLoader(TEXTURE_LOCATION);
        shaders = new ShaderLoader(SHADER_LOCATION);
        languages = new LanguageLoader(LANGUAGE_LOCATION);
        data = new DataLoader();
    }

    /**
     * Loads the default Resources
     */
    public static void load(){
        shaders.load();
        textures.load();
        languages.load();
        data.load();
    }

}
