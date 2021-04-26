package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.ui.UiScene;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.util.Globals;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * Contains the ui that is shown in game
 * @author Virt
 */
public class GameUI extends UiScene {

    private int lastFPS;

    private Label fps;

    public GameUI() {
        super(20);
    }

    @Override
    public void build() {
        background = new Vector4f(0, 0, 0, 0);

        float fpsSpacing = 0.1f;
        fps = new Label(0 + fpsSpacing, camera.getYUnits() - 0.5f - fpsSpacing, 10, 0.5f, 0.5f, "-", false, true);

        addComponent(fps);

    }

    @Override
    public void update(){
        super.update();

        if (Globals.getRendering().getFps() != lastFPS){
            fps.setText("" + Globals.getRendering().getFps());
            lastFPS = Globals.getRendering().getFps();
        }
    }
}
