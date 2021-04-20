package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.ui.components.base.PositionedComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import org.joml.Vector4f;

/**
 * This class represents a gui component which is just a label
 * @author Virt
 */
public class Label extends PositionedComponent {

    protected Text text;
    private boolean midded;
    private boolean centered;

    /**
     * Creates a label
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the label (only relevant if centered)
     * @param height height of the label (only relevant if midded)
     * @param scale scale of the font
     * @param text text to display
     * @param centered whether the text is centered
     * @param midded whether the text is midded (vertically centered)
     */
    public Label(float x, float y, float width, float height, float scale, String text, boolean centered, boolean midded) {
        super(x, y, width, height);

        this.midded = midded;
        this.centered = centered;

        this.text = new Text(text, new Vector4f(1, 1, 1, 1), x, y, scale);
    }

    @Override
    public void init() {
        text.setCamera(context.getCamera());
        text.init();
        this.text.setPosition(centered ? x + width / 2 - this.text.getWidth() / 2 : x, midded ? y + height / 2 - this.text.getHeight() / 2 : y);
    }

    @Override
    public void update(float delta) {

    }

    /**
     * Returns the width of the text
     * @return width of the text
     */
    public float getTextWidth(){
        return text.getWidth();
    }

    @Override
    public void draw() {
        text.draw();
    }
}
