package ch.virtbad.serint.client.ui.components.base;

/**
 * This class represents a ui component with a position and a width and height
 * @author Virt
 */
public abstract class PositionedComponent extends Component{

    protected float x, y, width, height;

    /**
     * Creates a positioned component
     * @param x x coordinate
     * @param y y coordinate
     * @param width width
     * @param height height
     */
    public PositionedComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
