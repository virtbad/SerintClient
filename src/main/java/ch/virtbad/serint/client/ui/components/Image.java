package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.base.Component;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;

/**
 * This class represents a gui component which is an image
 * @author Virt
 */
public class Image extends QuadComponent {

    private String textureResource;
    private Texture texture;

    /**
     * Creates an image
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the image
     * @param height height of the image
     * @param image resource location of the image
     */
    public Image(float x, float y, float width, float height, String image) {
        super(x, y, width, height, "image", true);
        this.textureResource = image;
    }

    @Override
    public void init() {
        super.init();
        texture = ResourceHandler.getTextures().get(textureResource);
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
    }

    @Override
    public void update(float delta) {

    }
}
