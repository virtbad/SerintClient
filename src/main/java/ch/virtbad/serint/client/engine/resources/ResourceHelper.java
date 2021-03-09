package ch.virtbad.serint.client.engine.resources;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * This class helps with a few basic resource operations, like loading files
 * @author Virt
 */
@Slf4j
public class ResourceHelper {

    /**
     * Returns the InputStream of a file contained in a classpath
     * @param path path of resource in the classpath
     * @return InputStream to be processed
     */
    public static InputStream getClasspathResource(String path){
        log.debug("Fetching Classpath Resource {}", path);
        return ResourceHelper.class.getResourceAsStream(path);
    }

    /**
     * Fetches a resource from the local filesystem
     * @param path path to fetch from
     * @return stream to be processed
     */
    public static InputStream getFilesystemResource(String path){
        log.debug("Fetching Filesystem File: {}", path);
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            log.debug("Failed to fetch Filesystem File {}", path);
            return null;
        }
    }

    /**
     * Returns the InputStream as an Image
     * @param resource InputStream to get image from
     * @return Image if loaded correctly, null if not able to load
     */
    public static BufferedImage getImage(InputStream resource){
        if (resource == null) return null;

        try {
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the InputStream as Text
     * @param resource InputStream to load from
     * @return String if loaded correctly, null if not able to load
     */
    public static String getText(InputStream resource){
        if (resource == null) return null;

        Scanner scanner = new Scanner(resource);
        scanner.useDelimiter("\\A");
        return scanner.next();
    }

}
