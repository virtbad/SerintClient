package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import lombok.Setter;

/**
 * This class represents button component
 * @author Virt
 */
public class Button extends QuadComponent {

    @Setter
    private BasicEvent event;

    private Texture texture;

    private boolean hovered;

    /**
     * Creates a button
     * @param x x coordinate
     * @param y y coordinate
     * @param width width
     * @param height height
     */
    public Button(float x, float y, float width, float height) {
        super(x, y, width, height, "button", true);
    }

    @Override
    public void init() {
        super.init();

        texture = ResourceHandler.getTextures().get("button");
    }

    @Override
    protected void uploadUniforms() {
        shader.uploadInt("state", hovered ? 1 : 0);
        texture.bind();
    }

    @Override
    public void update(float delta) {
        hovered = UiHelper.mouseHovering(x, y, width, height, context);
    }
}
