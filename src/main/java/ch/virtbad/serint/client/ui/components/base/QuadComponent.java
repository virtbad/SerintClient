package ch.virtbad.serint.client.ui.components.base;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * This class represents a ui component that is a quad and is rendered
 * @author Virt
 */
public abstract class QuadComponent extends PositionedComponent{

    protected Mesh mesh;
    protected Shader shader;
    private Matrix4f worldMatrix;

    private String shaderResource;
    private boolean textured;

    /**
     * Creates a quad component
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the component
     * @param height height of the component
     * @param shaderResource shader that is to be used
     * @param textured whether the component is textured
     */
    public QuadComponent(float x, float y, float width, float height, String shaderResource, boolean textured) {
        super(x, y, width, height);
        this.shaderResource = shaderResource;
        this.textured = textured;
    }

    @Override
    public void init() {
        shader = ResourceHandler.getShaders().get(shaderResource);

        if (textured) mesh = new Mesh(MeshHelper.createTexturedQuadVertices(width, height), MeshHelper.createQuadIndices());
        else mesh = new Mesh(MeshHelper.createQuadVertices(width, height), MeshHelper.createQuadIndices());

        mesh.init();
        setAttributePointers();

        updateWorldMatrix();
    }

    /**
     * Sets the attribute pointers for the mesh
     */
    protected void setAttributePointers(){
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, (textured ? 4 : 2) * Float.BYTES, 0);
        if (textured) mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    /**
     * Refreshes the world matrix, used for drawing the component at the correct location
     */
    protected void updateWorldMatrix(){
        worldMatrix = Camera.createWorldTranslationMatrix(0, 1, 1, x, y, 0);
    }

    /**
     * Uploads the uniforms for the shader
     */
    protected void uploadUniforms(){

    }

    @Override
    public void draw() {
        // Binds and modifies Shader
        shader.bind();
        shader.uploadMatrix("worldMatrix", worldMatrix);
        shader.uploadMatrix("viewMatrix", context.getCamera().getViewMatrix());
        uploadUniforms();

        // Draws Mesh
        mesh.draw();
    }
}
