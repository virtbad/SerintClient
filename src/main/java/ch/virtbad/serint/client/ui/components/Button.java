package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.events.EventHelper;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import ch.virtbad.serint.client.util.Time;
import lombok.Setter;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

/**
 * This class represents button component
 * @author Virt
 */
public class Button extends QuadComponent {

    @Setter
    private BasicEvent event;

    private Texture texture;

    private boolean hovered;
    private boolean pressed;

    private Text text;

    /**
     * Creates a button
     * @param x x coordinate
     * @param y y coordinate
     * @param width width
     * @param height height
     */
    public Button(float x, float y, float width, float height, String text) {
        super(x, y, width, height, "button", true);

        this.text = new Text(text, new Vector4f(1, 1, 1, 1), 0, 0, 0.5f);
    }

    @Override
    public void init() {
        super.init();

        texture = ResourceHandler.getTextures().get("button");

        text.setCamera(context.getCamera());
        text.init();
        text.setPosition(x + width / 2 - text.getWidth() / 2, y + height / 2 - text.getHeight() / 2);
    }

    @Override
    protected void uploadUniforms() {
        shader.uploadInt("state", hovered ? (pressed ? 2 : 1) : 0);
        texture.bind();
    }

    @Override
    public void update(float delta) {
        boolean hovering = UiHelper.mouseHovering(x, y, width, height, context);
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
    }

    @Override
    public void draw() {
        super.draw();

        text.draw();
    }
}
