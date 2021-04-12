package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.Getter;
import lombok.Setter;

/**
 * This class defines the basic structure of rendered scenes
 * @author Virt
 */
public abstract class Scene {

    @Setter
    protected Keyboard keyboard;
    @Setter
    protected Mouse mouse;

    /**
     * Is called when the scene should be initialized
     */
    public abstract void init(int width, int height);

    /**
     * Is called when the scene should update its components
     */
    public abstract void update();

    /**
     * Is called when the scene should draw
     */
    public abstract void draw();

    /**
     * Called when the scene / window is resized
     * @param width width of the scene
     * @param height height of the scene
     */
    public abstract void resized(int width, int height);
}
