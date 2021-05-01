package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.ui.components.font.Text;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * This class represents the scene that is shown when the game is loading.
 * @author Virt
 */
public class LoadingScene extends Scene {

    private Camera camera;

    private Text text = null;

    @Override
    public void init(int width, int height) {
        camera = new Camera(20, 20);
        camera.setScreenSize(width, height);
    }

    /**
     * Enables the text showing the loading progress.
     * For it to work, assets regarding font rendering have to be loaded.
     */
    public void addLoadingText(){
        text = new Text("Initializing", new Vector4f(1, 1, 1, 1), 0, 0, 1);
        text.setCamera(camera);
        text.init();
        text.setPosition(camera.getXUnits() / 2 - text.getWidth() / 2, camera.getYUnits() / 2 - text.getHeight() / 2);
    }

    /**
     * Sets the loading Test
     * @param s string of the loading text
     */
    public void setLoadingText(String s){
        this.text.setText(s);
        text.setPosition(camera.getXUnits() / 2 - text.getWidth() / 2, camera.getYUnits() / 2 - text.getHeight() / 2);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        glClearColor(0, 0, 0, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        if (text != null) text.draw();

    }

    @Override
    public void resized(int width, int height) {
        camera.setScreenSize(width, height);

        if (text != null) text.setPosition(camera.getXUnits() / 2 - text.getWidth() / 2, camera.getYUnits() / 2 - text.getHeight() / 2);
    }

    @Override
    public void shown() {

    }
}
