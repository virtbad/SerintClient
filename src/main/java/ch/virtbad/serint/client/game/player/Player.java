package ch.virtbad.serint.client.game.player;

import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.MovedLocation;
import lombok.Getter;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * This class represents an instance of a player that is also drawable on the screen
 * @author Virt
 */
public class Player extends MeshedGameObject {

    @Getter
    private int id;
    private Vector3f color;
    @Getter
    private String name;

    /**
     * Creates a player instance
     * @param id id of the player
     * @param color colour of the player (as a float vector)
     * @param name name of the player
     */
    public Player(int id, Vector3f color, String name) {
        super(MeshHelper.createQuadVertices(1, 1), MeshHelper.createQuadIndices(), "player", new MovedLocation(), 0);

        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 2 * Float.BYTES, 0);
    }

    @Override
    protected void uploadUniforms() {
        shader.uploadVector("color", color);
    }

    @Override
    public void update(float updateDelta) {
        location.time(updateDelta);
        updateWorldMatrix();
    }

    /**
     * Gets the real player location
     * @return location of the player
     */
    public MovedLocation getLocation(){
        return ((MovedLocation)location);
    }
}