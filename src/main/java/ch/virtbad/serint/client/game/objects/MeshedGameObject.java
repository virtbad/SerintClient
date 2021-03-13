package ch.virtbad.serint.client.game.objects;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.resources.ResourceHelper;
import ch.virtbad.serint.client.engine.resources.Shader;
import ch.virtbad.serint.client.engine.resources.Texture;
import ch.virtbad.serint.client.game.positioning.FixedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import org.joml.Matrix4f;

/**
 * This class is a game object that has basic drawing features and has a mesh attached
 * @author Virt
 */
public class MeshedGameObject extends PositionedGameObject {

    private String shaderResource;

    protected Mesh mesh;
    protected Shader shader;

    protected Matrix4f worldMatrix;

    /**
     * Creates a meshed Game Object
     * @param vertices vertices of the mesh
     * @param indices indices of the mesh
     * @param shaderResource name of the shader
     * @param location location of the mesh
     * @param layer layer where the mesh should be
     */
    public MeshedGameObject(float[] vertices, int[] indices, String shaderResource, FixedLocation location, int layer) {
        super(location, layer);

        this.shaderResource = shaderResource;
        this.mesh = new Mesh(vertices, indices);
    }

    @Override
    public void init() {
        super.init();

        // Fetch resources
        shader = ResourceHandler.getShaders().get(shaderResource);

        // Setup Mesh
        mesh.init();
        setAttribPointers();

        // Create world Matrix
        updateWorldMatrix();
    }

    /**
     * Resets the world matrix with the current position and layer and scale one
     */
    protected void updateWorldMatrix(){
        worldMatrix = Camera.createWorldTranslationMatrix(0, 1, 1, location.getPosX(), location.getPosY(), layer);
    }

    /**
     * Is called when the Attrib pointers of the mesh should be set
     */
    protected void setAttribPointers() {

    }

    /**
     * Is called when the uniforms should be uploaded onto the shader
     */
    protected void uploadUniforms() {

    }

    @Override
    public void draw() {

        // Binds and modifies Shader
        shader.bind();
        shader.uploadMatrix4f("worldMatrix", worldMatrix);
        shader.uploadMatrix4f("viewMatrix", context.getCamera().getViewMatrix());
        uploadUniforms();

        // Draws Mesh
        mesh.draw();

        super.draw();
    }
}
