package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.base.Component;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class represents a gui component which is a background image
 * @author Virt
 */
public class BackgroundImage extends Component {

    private Mesh mesh;
    private Shader shader;
    private Texture texture;
    private String textureResource;

    /**
     * Creates a background image
     * @param textureResource resource of the image
     */
    public BackgroundImage(String textureResource) {
        this.textureResource = textureResource;
    }

    @Override
    public void init() {
        shader = ResourceHandler.getShaders().get("backgroundimage");
        texture = ResourceHandler.getTextures().get(textureResource);

        mesh = new Mesh(MeshHelper.createFramebufferVertices(), MeshHelper.createQuadIndices());
        mesh.init();
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw() {
        shader.bind();
        texture.bind();
        shader.uploadFloat("width", context.getCamera().getXUnits());
        shader.uploadFloat("height", context.getCamera().getYUnits());

        mesh.draw();
    }
}
