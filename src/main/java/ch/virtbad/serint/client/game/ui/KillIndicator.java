package ch.virtbad.serint.client.game.ui;

import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import ch.virtbad.serint.client.util.Time;
import lombok.Setter;

/**
 * This GUI Component indicates whether the player is able to kill someone
 * @author Virt
 */
public class KillIndicator extends QuadComponent {

    private Texture texture;

    @Setter
    private float lastKill;

    /**
     * Creates an image
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the image
     * @param height height of the image
     */
    public KillIndicator(float x, float y, float width, float height) {
        super(x, y, width, height, "kill", true);
    }

    @Override
    public void init() {
        super.init();
        texture = ResourceHandler.getTextures().get("kill");
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadFloat("time", Time.getSeconds());
        shader.uploadFloat("progress", Math.min((Time.getSeconds() - lastKill) / Game.KILL_COOLDOWN, 1));
        shader.uploadInt("state", ((Time.getSeconds() - lastKill) / Game.KILL_COOLDOWN > 1 ? 1 : 0));
    }

    @Override
    public void update(float delta) {

    }
}
