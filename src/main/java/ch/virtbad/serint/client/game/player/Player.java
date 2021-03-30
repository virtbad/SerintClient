package ch.virtbad.serint.client.game.player;

import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.Texture;
import ch.virtbad.serint.client.game.collisions.AABB;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.MovedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.Getter;
import lombok.Setter;
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
    @Getter
    private AABB bounds;
    private final float xPadding = 0.12f;

    @Setter @Getter
    private PlayerAttributes attributes;

    private Texture texture;

    /**
     * Creates a player instance
     * @param id id of the player
     * @param color colour of the player (as a float vector)
     * @param name name of the player
     */
    public Player(int id, Vector3f color, String name) {
        super(MeshHelper.createTexturedQuadVertices(1, 1), MeshHelper.createQuadIndices(), "player", new MovedLocation(), 0);

        this.id = id;
        this.name = name;
        this.color = color;
        this.bounds = new AABB(location.getPosX() + xPadding, location.getPosY(), 1 - xPadding * 2, 0.2f);
        attributes = new PlayerAttributes();
    }

    @Override
    public void init() {
        texture = ResourceHandler.getTextures().get("player"); // TODO: Fix
        super.init();
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadVector("color", color);
    }

    @Override
    public void update(float updateDelta) {
        updateWorldMatrix();
        bounds.setPosition(location.getPosX() + xPadding, location.getPosY());
    }



    /**
     * Gets the real player location
     * @return location of the player
     */
    public MovedLocation getLocation(){
        return ((MovedLocation)location);
    }
}
