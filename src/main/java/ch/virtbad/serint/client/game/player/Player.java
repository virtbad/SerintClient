package ch.virtbad.serint.client.game.player;

import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.game.collisions.AABB;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.MovedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.font.Text;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * This class represents an instance of a player that is also drawable on the screen
 *
 * @author Virt
 */
public class Player extends MeshedGameObject {
    private static final int FRAME_AMOUNT = 4;
    private static final float FRAME_DELAY = 0.1f;
    private static final float BLINKING_PROBABILITY = 0.1f;
    private static final int BLINKING_FRAME = 4;
    private static final int BLINKING_FRAME_AMOUNT = 2;

    @Getter
    private int id;
    @Getter
    private Vector3f color;
    @Getter
    private String name;
    @Getter
    private AABB bounds;
    private final float xPadding = 0.12f;

    @Setter
    @Getter
    private PlayerAttributes attributes;

    private Texture texture;
    private Vector2f part;
    private float delayPassed;
    private boolean blinking;

    private Text nameTag;
    private boolean init;

    /**
     * Creates a player instance
     *
     * @param id    id of the player
     * @param color colour of the player (as a float vector)
     * @param name  name of the player
     */
    public Player(int id, Vector3f color, String name) {
        super(MeshHelper.createTexturedQuadVertices(1, 1), MeshHelper.createQuadIndices(), "player", new MovedLocation(), 0);

        this.id = id;
        this.name = name;
        this.color = color;
        this.bounds = new AABB(location.getPosX() + xPadding, location.getPosY(), 1 - xPadding * 2, 0.2f);
        attributes = new PlayerAttributes();
        this.part = new Vector2f(1, 0);

        nameTag = new Text(name, new Vector4f(1, 1, 1, 1), 0, 0, 0.25f);
    }

    @Override
    public void init() {
        texture = ResourceHandler.getTextures().get("player");

        nameTag.setCamera(context.getCamera());
        nameTag.init();

        nameTag.setPosition(location.getPosX() + 0.5f - nameTag.getWidth() / 2, location.getPosY() + 1);

        super.init();
        init = true;
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadVec3("color", color);
        shader.uploadVec2("part", part);
    }

    @Override
    public void update(float updateDelta) {
        updateWorldMatrix();
        nameTag.setPosition(location.getPosX() + 0.5f - nameTag.getWidth() / 2, location.getPosY() + 1);
        bounds.setPosition(location.getPosX() + xPadding, location.getPosY());

        // Hovering and Blinking animation (could also be done in shader)
        delayPassed += updateDelta;
        if (delayPassed >= FRAME_DELAY) {

            if (blinking) {
                if (part.x < BLINKING_FRAME) {
                    part.x = BLINKING_FRAME;
                } else if (part.x + 1 < BLINKING_FRAME + BLINKING_FRAME_AMOUNT) {
                    part.x++;
                } else {
                    blinking = false;
                    part.x = 0; // Do not wait another frame
                }
            } else {
                if (part.x >= FRAME_AMOUNT - 1) part.x = 0;
                else part.x++;

                if (part.x == FRAME_AMOUNT -1 && Math.random() < BLINKING_PROBABILITY) {
                    blinking = true;
                }
            }

            delayPassed = 0;
        }

        // Direction Animation
        MovedLocation loc = ((MovedLocation) location);

        if (loc.getVelocityX() > 0 && loc.getVelocityY() == 0) part.y = 7; // Right
        else if (loc.getVelocityX() < 0 && loc.getVelocityY() == 0) part.y = 6; // Left
        else if (loc.getVelocityX() > 0 && loc.getVelocityY() > 0) part.y = 5; // Up Right
        else if (loc.getVelocityX() > 0 && loc.getVelocityY() < 0) part.y = 4; // Down Right
        else if (loc.getVelocityX() < 0 && loc.getVelocityY() > 0) part.y = 3; // Up Left
        else if (loc.getVelocityX() < 0 && loc.getVelocityY() < 0) part.y = 2; // Down Left
        else if (loc.getVelocityX() == 0 && loc.getVelocityY() > 0) part.y = 1; // Up
        else part.y = 0; // Down and Default
    }

    @Override
    public void draw() {
        super.draw();
        if(init) nameTag.draw();
    }

    /**
     * Gets the real player location
     *
     * @return location of the player
     */
    public MovedLocation getLocation() {
        return ((MovedLocation) location);
    }
}
