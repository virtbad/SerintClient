package ch.virtbad.serint.client.engine.resources.framebuffers;

import lombok.Getter;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author Virt
 * This class handles one framebuffer without any attributes
 */
public abstract class FrameBuffer {

    @Getter
    private final int id;

    /**
     * Creates the framebuffer
     */
    public FrameBuffer() {
        id = glGenFramebuffers();
    }

    /**
     * Binds the framebuffer
     */
    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    /**
     * Binds the default framebuffer
     */
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Destroys the framebuffer (removes it from memory)
     */
    public void destroy(){
        glDeleteFramebuffers(id);
    }
}
