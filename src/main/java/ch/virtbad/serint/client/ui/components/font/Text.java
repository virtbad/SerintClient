package ch.virtbad.serint.client.ui.components.font;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * This class handles and manages a mesh, representing a text
 */
public class Text {

    @Getter @Setter
    private Vector4f color;
    @Getter
    private String text;

    private Mesh mesh;
    private Shader shader;
    private Texture texture;

    private float modelWidth, modelHeight;

    private Matrix4f worldMatrix;
    @Getter
    private float scale;
    @Getter
    private float rotation;
    @Getter
    private float x, y;

    @Setter
    private Camera camera;

    /**
     * Creates a text (without initializing it)
     * @param text string to use
     * @param color color of the string
     * @param x x position of the string
     * @param y y position of the string
     * @param scale scale of the string
     */
    public Text(String text, Vector4f color, float x, float y, float scale) {
        this.text = text;
        this.color = color;

        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    /**
     * Initializes the mesh (a camera still needs to be set)
     */
    public void init(){
        shader = ResourceHandler.getShaders().get("font");
        texture = ResourceHandler.getTextures().get("font");

        mesh = new Mesh(FontOperations.generateOneLineVertices(text), FontOperations.generateOneLineIndices(text));
        modelHeight = FontOperations.getOneLineHeight(text);
        modelWidth = FontOperations.getOneLineWidth(text);

        mesh.init();

        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);

        updateMatrix();
    }

    /**
     * Resets the string of that text
     * @param text new string
     */
    public void setText(String text){
        this.text = text;
        mesh.updateVertices(FontOperations.generateOneLineVertices(text));
        mesh.updateIndices(FontOperations.generateOneLineIndices(text));

        modelHeight = FontOperations.getOneLineHeight(text);
        modelWidth = FontOperations.getOneLineWidth(text);
    }

    /**
     * Sets the rotation of the text
     * @param rotation rotation of the text
     */
    public void setRotation(float rotation){
        this.rotation = rotation;
        updateMatrix();
    }

    /**
     * Sets the scale of the text
     * @param scale scale of the text
     */
    public void setScale(float scale){
        this.scale = scale;
        updateMatrix();
    }

    /**
     * Sets the position of the text
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateMatrix();
    }

    /**
     * Returns the width of the text
     * @return width of the text
     */
    public float getWidth(){
        return modelWidth * scale;
    }

    /**
     * Returns the height of the text
     * @return height of the text
     */
    public float getHeight(){
        return  modelHeight * scale;
    }

    /**
     * Refreshes the world matrix of the text
     */
    private void updateMatrix(){
        worldMatrix = Camera.createWorldTranslationMatrix(rotation, scale, scale, x, y, 0);
    }

    /**
     * Draws the text
     */
    public void draw(){
        shader.bind();

        shader.uploadVec4("color", color);

        shader.uploadMatrix("worldMatrix", worldMatrix);
        shader.uploadMatrix("viewMatrix", camera.getViewMatrix());

        texture.bind();

        mesh.draw();
    }

}
