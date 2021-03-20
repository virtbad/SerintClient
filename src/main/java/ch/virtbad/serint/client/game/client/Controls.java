package ch.virtbad.serint.client.game.client;

import ch.virtbad.serint.client.game.GameContext;
import ch.virtbad.serint.client.game.player.Player;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

/**
 * This class handles controls for a player
 * @author Virt
 */
public class Controls {

    private static final int KEY_UP = GLFW.GLFW_KEY_UP;
    private static final int KEY_DOWN = GLFW.GLFW_KEY_DOWN;
    private static final int KEY_LEFT = GLFW.GLFW_KEY_LEFT;
    private static final int KEY_RIGHT = GLFW.GLFW_KEY_RIGHT;

    private static final float SPEED = 5f; // = 5 tiles per second

    private final GameContext context;

    @Setter
    private Player player;

    /**
     * Creates a controls
     * @param context context of the game
     */
    public Controls(GameContext context) {
        this.context = context;
    }

    /**
     * Calculates and applies the movement
     * @return whether the velocities have changed (to send position updates)
     */
    public boolean doMovement(){
        if (player == null) return false;

        float velocityX = 0;
        float velocityY = 0;

        if (context.getKeyboard().isDown(KEY_UP)) velocityY = SPEED;
        if (context.getKeyboard().isDown(KEY_DOWN)) velocityY = -SPEED;
        if (context.getKeyboard().isDown(KEY_RIGHT)) velocityX = SPEED;
        if (context.getKeyboard().isDown(KEY_LEFT)) velocityX = -SPEED;

        boolean updated = player.getLocation().getVelocityX() != velocityX || player.getLocation().getVelocityY() != velocityY;

        player.getLocation().setVelocityX(velocityX);
        player.getLocation().setVelocityY(velocityY);

        return updated;
    }

}
