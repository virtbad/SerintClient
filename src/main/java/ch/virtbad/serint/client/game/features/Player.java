package ch.virtbad.serint.client.game.features;

import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.positioning.MovedLocation;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * @author Virt
 */
public class Player extends MeshedGameObject {

    public Player() {
        super(MeshHelper.createQuadVertices(1, 1), MeshHelper.createQuadIndices(), "player", new MovedLocation(), 0);
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 2 * Float.BYTES, 0);
    }

    @Override
    public void update(float updateDelta) {
        if (context.getKeyboard().isDown(GLFW.GLFW_KEY_UP)){
            ((MovedLocation) location).setVelocityX(1);
            ((MovedLocation) location).setVelocityY(1);
        } else {
            ((MovedLocation) location).setVelocityX(0);
            ((MovedLocation) location).setVelocityY(0);
        }

        location.time(updateDelta);
        updateWorldMatrix();
    }
}
