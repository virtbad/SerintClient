package ch.virtbad.serint.client.game.ui;

import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.game.player.Player;
import ch.virtbad.serint.client.game.player.PlayerAttributes;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import ch.virtbad.serint.client.util.Time;
import lombok.Setter;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * This UI Component shows the current stats about a player
 * @author Virt
 */
public class HudIndicator extends QuadComponent {

    private Texture texture;
    private Player player;

    private Text name;
    private boolean nameUpdated;

    /**
     * Creates an image
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the image
     * @param height height of the image
     */
    public HudIndicator(float x, float y, float width, float height) {
        super(x, y, width, height, "indicator", true);

        name = new Text("Name", new Vector4f(1, 1, 1, 1), x + 3 + 0.1f, y + 1 + 0.25f, 0.5f);
    }

    @Override
    public void init() {
        super.init();
        texture = ResourceHandler.getTextures().get("indicator");

        name.setCamera(context.getCamera());
        name.init();
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadFloat("time", Time.getSeconds());

        if (player == null) return;
        shader.uploadFloat("life", player.getAttributes().getHealth() / ((float) player.getAttributes().getMaxHealth()));
        shader.uploadFloat("speed", player.getAttributes().getSpeed() / player.getAttributes().getMaxSpeed());
        shader.uploadFloat("vision", player.getAttributes().getVision() / player.getAttributes().getMaxVision());
        shader.uploadInt("visionBoosted", player.getAttributes().isVisionBoosted() ? 1 : 0);
        shader.uploadInt("speedBoosted", player.getAttributes().isSpeedBoosted() ? 1 : 0);
        shader.uploadVec3("avatarColor", player.getColor());
    }

    @Override
    public void update(float delta) {
        if (nameUpdated){
            nameUpdated = false;

            name.setText(player.getName());
        }
    }

    @Override
    public void draw() {
        super.draw();
        name.draw();
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);

        name.setPosition(x + 3 + 0.1f, y + 1 + 0.25f);
    }

    /**
     * Sets the player to display stats for
     * @param player player
     */
    public void setPlayer(Player player) {
        this.player = player;

        nameUpdated = true;
    }
}
