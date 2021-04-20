package ch.virtbad.serint.client.ui.lang;

import lombok.Getter;

import java.util.HashMap;

/**
 * This class contains a translation into a certain language
 * @author Virt
 */
public class Language {

    @Getter
    private String identifier;
    @Getter
    private String name;

    private HashMap<String, String> content;

    /**
     * Returns the translation for a given key
     * @param key key to get translation for
     * @return translation
     */
    public String getString(String key){
        return content.get(key);
    }
}
