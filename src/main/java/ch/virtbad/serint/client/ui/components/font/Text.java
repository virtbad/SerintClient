package ch.virtbad.serint.client.ui.components.font;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.Setter;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class Text {

    private String string;
    private Mesh mesh;

    private Shader shader;
    private Texture texture;

    private Vector4f color;

    @Setter
    private Camera camera;

    public Text(String text, Vector4f color) {
        this.string = text;
        this.color = color;
    }

    public void init(){
        shader = ResourceHandler.getShaders().get("font");
        texture = ResourceHandler.getTextures().get("font");

        mesh = new Mesh(FontOperations.generateOneLineVertices(string), FontOperations.generateOneLineIndices(string));
        mesh.init();

        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    public void draw(){
        shader.bind();

        shader.uploadVec4("color", color);

        shader.uploadMatrix("worldMatrix", Camera.createWorldTranslationMatrix(0, 0.5f, 0.5f, 1, 1, 0));
        shader.uploadMatrix("viewMatrix", camera.getViewMatrix());

        texture.bind();

        mesh.draw();
    }

}
