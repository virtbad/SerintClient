package ch.virtbad.serint.engine.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * This class helps with a few basic resource operations, like loading files
 * @author Virt
 */
public class ResourceHelper {

    /**
     * Returns the InputStream of a file contained in a classpath
     * @param path path of resource in the classpath
     * @return InputStream to be processed
     */
    public static InputStream getClasspathResource(String path){
        return ResourceHelper.class.getResourceAsStream(path);
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
