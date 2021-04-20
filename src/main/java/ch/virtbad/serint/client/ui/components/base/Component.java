package ch.virtbad.serint.client.ui.components.base;

import ch.virtbad.serint.client.ui.Context;
import lombok.Setter;

/**
 * This class represents a basic ui component
 * @author Virt
 */
public abstract class Component {

    @Setter
    protected Context context;

    /**
     * Initializes the component. OpenGL calls should only be called in here
     */
    public abstract void init();

    /**
     * Updates the component
     * @param delta time difference between updates
     */
    public abstract void update(float delta);

    /**
     * Draws the component onto the screen
     */
    public abstract void draw();

}
