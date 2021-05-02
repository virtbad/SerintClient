package ch.virtbad.serint.client.ui.lang;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import ch.virtbad.serint.client.engine.resources.ResourceIndicator;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This class loads and manages Languages
 * @author Virt
 */
@Slf4j
public class LanguageLoader {

    private final String path;
    private ResourceIndicator indicator;

    private HashMap<String, Language> languages;

    private String selected;
    private String defaultLanguage;

    /**
     * Initializes the LanguageLoader
     * @param path path to search for languages
     */
    public LanguageLoader(String path) {
        this.path = path;
        languages = new HashMap<>();
    }

    /**
     * Loads all languages from the default path
     */
    public void load(){
        log.info("Starting to load Languages");

        InputStream indStream = ResourceHelper.getClasspathResource(path + ResourceIndicator.DEFAULT_NAME);
        if (indStream == null) throw new IllegalStateException("Cannot load Languages from missing indicator file!");
        indicator = new Gson().fromJson(new InputStreamReader(indStream), ResourceIndicator.class);

        log.info("Default Language is {}", indicator.getDefaultResource());

        loadLanguage(path + indicator.forgeFilename(indicator.getDefaultResource()), indicator.getDefaultResource());
        defaultLanguage = indicator.getDefaultResource();

        for (String resource : indicator.getResources()) {
            loadLanguage(path + indicator.forgeFilename(resource), resource);
        }

        selected = ConfigHandler.getConfig().getLanguage();
        log.info("Selected Language is {}", selected);
    }

    /**
     * Loads a language
     * @param file language to load
     * @param name name of the language
     */
    public void loadLanguage(String file, String name){
        log.info("Loading Language: {}", file);

        InputStream resource = ResourceHelper.getClasspathResource(file);
        if (resource == null) log.warn("Failed to load language {} from {}", file, name);
        else {
            Language language = new Gson().fromJson(new InputStreamReader(resource), Language.class);
            languages.put(name, language);
        }
    }

    /**
     * Returns a translation for a key
     * @param key key to find translation for
     * @return translation
     */
    public String getString(String key){
        String translation;
        if (languages.containsKey(selected)){
            translation = languages.get(selected).getString(key);
            if (translation == null) translation = languages.get(defaultLanguage).getString(key);
        }else translation = languages.get(defaultLanguage).getString(key);

        if (translation == null){
            log.warn("Unable to find Translation for key \"{}\"", key);
            translation = key;
        }

        return translation;
    }

    public String getSelectedIdentifier(){
        return languages.containsKey(selected) ? selected : defaultLanguage;
    }

    /**
     * Returns all languages loaded
     * @return loaded languages
     */
    public Language[] getLanguages(){
        return languages.values().toArray(new Language[0]);
    }
}
