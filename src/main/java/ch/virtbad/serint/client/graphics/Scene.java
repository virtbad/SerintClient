package ch.virtbad.serint.client.graphics;

/**
 * This class defines the basic structure of rendered scenes
 * @author Virt
 */
public abstract class Scene {

    /**
     * Is called when the scene should be initialized
     */
    public abstract void init();

    /**
     * Is called when the scene should update its components
     */
    public abstract void update();

    /**
     * Is called when the scene should draw
     */
    public abstract void draw();
}
