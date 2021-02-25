package ch.virtbad.serint.engine.resources;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class stores code for using and creating textures
 * @author Virt
 */
public class Texture {
    @Getter
    private final int id;

    @Getter
    private final int width, height;

    /**
     * Loads each pixel with its colours into a byte array, ready to use for the gpu
     * @param image image to load
     * @return array containing pixels in RGBA fashion
     */
    private static byte[] getBytesFromImage(BufferedImage image){
        if (image == null) throw new IllegalStateException("Cannot get texture from null Image");

        byte[] bytes = new byte[image.getWidth() * image.getHeight() * 4]; // Times four because of ARGB

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int c = image.getRGB(j, i);

                //    Rows                         Column    Offset
                bytes[i * image.getWidth() * 4 +   j * 4      ] = (byte) (c >>> 16 & 0xFF);   // Shift 2 bytes and get last   -> Get second Byte
                bytes[i * image.getWidth() * 4 +   j * 4 +   1] = (byte) (c >>> 8 & 0xFF);    // Shift 1 Byte and get last    -> Get third Byte
                bytes[i * image.getWidth() * 4 +   j * 4 +   2] = (byte) (c & 0xFF);          // Only Get last Byte           -> Get fourth Byte
                bytes[i * image.getWidth() * 4 +   j * 4 +   3] = (byte) (c >>> 24);          // Shift 3 Bytes                -> Get first Byte
            }
        }

        return bytes;
    }

    /**
     * Creates a Texture from an image
     * @param image image to use
     */
    public Texture(BufferedImage image){
        this(getBytesFromImage(image), image.getWidth(), image.getHeight());
    }

    /**
     * Creates a Texture from a byte array
     * @param bytes bytes to use for the image in RGBA
     * @param width width of the image
     * @param height height of the image
     */
    public Texture(byte[] bytes, int width, int height){
        this.width = width;
        this.height = height;

        // Creates and uploads Texture onto the Graphics Card
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Create Direct buffer because opengl seems only to take these
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    /**
     * Binds the texture for further usage
     */
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

}
