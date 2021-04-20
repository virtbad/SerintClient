package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.events.EventHelper;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.base.PositionedComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import lombok.Setter;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

/**
 * This class represents a gui component which is a label and link
 * @author Virt
 */
public class LabelLink extends Label {

    private static final float IDLE = 0.6f;
    private static final float HOVERING = 0.5f;
    private static final float CLICKED = 0.3f;

    private boolean hovered, pressed;

    @Setter
    private BasicEvent event;

    /**
     * Creates a label link
     * See label constructor for parameter description
     */
    public LabelLink(float x, float y, float width, float height, float scale, String text, boolean centered, boolean midded) {
        super(x, y, width, height, scale, text, centered, midded);
        this.text.setColor(new Vector4f(IDLE, IDLE, IDLE, 1));
    }

    @Override
    public void update(float delta) {
        boolean hovering = UiHelper.mouseHovering(text.getX(), text.getY(), text.getWidth(), text.getHeight(), context);
        boolean pressing = context.getMouse().isDown(GLFW.GLFW_MOUSE_BUTTON_1);

        if (hovering){
            hovered = true;

            if (pressing){
                pressed = true;
            }else {
                if (pressed) EventHelper.emitEvent(event);

                pressed = false;
            }
        } else {
            hovered = false;
            pressed = false;
        }

        if (pressed) text.setColor(new Vector4f(CLICKED, CLICKED, CLICKED, 1));
        else if (hovered) text.setColor(new Vector4f(HOVERING, HOVERING, HOVERING, 1));
        else text.setColor(new Vector4f(IDLE, IDLE, IDLE, 1));
    }
}
