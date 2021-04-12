package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.framebuffers.TextureFrameBuffer;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.game.client.Lighting;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class GameRenderer {

    private Mesh mesh;
    private Shader shader;
    private TextureFrameBuffer map;
    private TextureFrameBuffer player;

    private final Lighting lighting;
    private final Camera gameCamera;

    public GameRenderer(int width, int height, Lighting lighting, Camera gameCamera){
        this.lighting = lighting;
        this.gameCamera = gameCamera;

        // Create Framebuffers
        map = new TextureFrameBuffer(width, height);
        player = new TextureFrameBuffer(width, height);

        // Create Rendering Mesh
        mesh = new Mesh(MeshHelper.createFramebufferVertices(), MeshHelper.createQuadIndices());
        mesh.init();
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);

        // Load Shader
        shader = ResourceHandler.getShaders().get("frame");
    }

    public void resize(int width, int height){
        map.resize(width, height);
        player.resize(width, height);
    }

    public void bindMap(){
        map.bind();
    }

    public void bindPlayer(){
        player.bind();
    }

    public void draw(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glClearColor(0, 0, 0, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        shader.bind();
        player.getTexture().bindToShader(shader, "player", 1);
        map.getTexture().bindToShader(shader, "map", 0); // Why does the sequence matter?

        shader.uploadVec2("gameViewportPosition", new Vector2f(gameCamera.getXPos(), gameCamera.getYPos()));
        shader.uploadVec2("gameViewportSize", new Vector2f(gameCamera.getXUnits(), gameCamera.getYUnits()));

        lighting.upload(shader);

        mesh.draw();
    }
}
