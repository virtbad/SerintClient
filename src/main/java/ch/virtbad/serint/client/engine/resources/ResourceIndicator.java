package ch.virtbad.serint.client.engine.resources;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * This class represents a resource indicator json found in the resource folders
 * @author Virt
 */
@Getter
public class ResourceIndicator {
    public static final String DEFAULT_NAME = "resources.json";

    private String ending;
    @SerializedName("default")
    private String defaultResource;
    private String[] resources;

    /**
     * Returns the name of a file located in this resource
     * @param file file to forge name
     * @return filename
     */
    public String forgeFilename(String file){
        return file + ending;
    }
}
