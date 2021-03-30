package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.graphics.Scene;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * @author Virt
 */
public class LoadingScene extends Scene {
    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        glClearColor(1, 1, 1, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resized(int width, int height) {

    }
}
