package ch.virtbad.serint.client.engine.resources.framebuffers;

import ch.virtbad.serint.client.engine.resources.textures.Texture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author Virt
 * This class handles a framebuffer which draws onto a texture
 */
@Slf4j
public class TextureFrameBuffer extends FrameBuffer {

    @Getter
    private final Texture texture;

    /**
     * Creates a textured framebuffer
     * @param width width of the texture
     * @param height height of the texture
     */
    public TextureFrameBuffer(int width, int height) {
        super();
        bind();

        texture = new Texture(width, height);
        texture.bind();

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) log.warn("Created Framebuffer is not complete, it may lead to visual errors!");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void destroy() {
        texture.destroy();
        super.destroy();
    }
}
