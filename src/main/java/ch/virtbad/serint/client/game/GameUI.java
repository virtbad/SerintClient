package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.game.player.Player;
import ch.virtbad.serint.client.game.ui.HudIndicator;
import ch.virtbad.serint.client.game.ui.KillIndicator;
import ch.virtbad.serint.client.ui.UiScene;
import ch.virtbad.serint.client.ui.components.Container;
import ch.virtbad.serint.client.ui.components.Image;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.util.Globals;
import org.joml.Vector4f;

/**
 * Contains the ui that is shown in game
 * @author Virt
 */
public class GameUI extends UiScene {

    private int lastFPS;
    private Label fps;

    Container hudContainer;

    HudIndicator hud;
    KillIndicator kill;

    public GameUI() {
        super(20);
    }

    @Override
    public void build() {
        // Avoid Black overlay
        background = new Vector4f(0, 0, 0, 0);

        // FPS Counter
        fps = new Label(camera.getXUnits() - 0.1f, camera.getYUnits() - 0.5f - 0.1f, 10, 0.5f, 0.5f, "-", false, true);
        fps.setContext(context);
        fps.init();

        // Ingame Hud
        hudContainer = new Container();
        addComponent(hudContainer);

        kill = new KillIndicator(camera.getXUnits() - 3 - 0.5f, 0.5f, 3, 3);
        hudContainer.addComponent(kill);

        hud = new HudIndicator(0.5f, camera.getYUnits() - 3.5f, 9, 3);
        hudContainer.addComponent(hud);
    }

    @Override
    public void draw(){
        super.draw();
        if (ConfigHandler.getConfig().isShowFps()) fps.draw();
    }

    @Override
    public void update(){
        super.update();

        if (ConfigHandler.getConfig().isShowFps()) {

            if (Globals.getRendering().getFps() != lastFPS) {
                fps.setText("" + Globals.getRendering().getFps());
                fps.setPosition(camera.getXUnits() - fps.getTextWidth() - 0.1f, camera.getYUnits() - 0.5f - 0.1f);
                lastFPS = Globals.getRendering().getFps();
            }
        }
    }

    /**
     * Sets the own player
     * @param player player
     */
    public void setOwn(Player player){
        hud.setPlayer(player);
    }

    /**
     * Sets when the last kill was triggered
     * @param last time in seconds
     */
    public void setLastKill(float last){
        kill.setLastKill(last);
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);

        fps.setPosition(camera.getXUnits() - fps.getTextWidth() - 0.1f, camera.getYUnits() - 0.5f - 0.1f);
        hud.setPosition(0.5f, camera.getYUnits() - 3.5f);
        kill.setPosition(camera.getXUnits() - 3 - 0.5f, 0.5f);
    }
}
