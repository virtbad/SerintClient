package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.UiScene;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Container;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.util.Time;
import org.joml.Vector4f;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * Contains the ui that is shown in game
 * @author Virt
 */
public class GameUI extends UiScene {

    private int lastFPS;

    private Label fps;

    private Container pauseMenu;
    private Container deathMenu;
    private Container loseMenu;
    private Container winMenu;


    private int seconds;


    public GameUI() {
        super(20);
    }


    private Label counter;
    private Label killerName;
    private Label lifeLeft;

    @Override
    public void build() {
        float TITLE_Y = camera.getYUnits() * 0.65f;
        float SUBTITLE_Y = TITLE_Y - 1.25f;
        float BUTTON_X = camera.getXUnits() / 2  - BUTTON_WIDTH / 2f;
        float BUTTON_Y = camera.getYUnits() * 0.475f ;

        background = new Vector4f(0, 0, 0, 0);

        float fpsSpacing = 0.1f;
        fps = new Label(0 + fpsSpacing, camera.getYUnits() - 0.5f - fpsSpacing, 10, 0.5f, 0.5f, "-", false, true);

        addComponent(fps);

        pauseMenu = new Container();
        deathMenu = new Container();
        loseMenu = new Container();
        winMenu = new Container();

        //pause menu

        Label pauseTitle = new Label(0, TITLE_Y,camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.pause.title"), true, true );
        Button optionsButton =  new Button(BUTTON_X, TITLE_Y - 2.5f, BUTTON_WIDTH, BUTTON_HEIGHT,ResourceHandler.getLanguages().getString("ui.pause.options"));
        Button leaveButton =  new Button(BUTTON_X, TITLE_Y - 4.5f, BUTTON_WIDTH, BUTTON_HEIGHT,ResourceHandler.getLanguages().getString("ui.pause.leave"));
        Button returnButton =  new Button(BUTTON_X, TITLE_Y - 6.5f, BUTTON_WIDTH, BUTTON_HEIGHT,ResourceHandler.getLanguages().getString("ui.pause.back"));

        pauseMenu.addComponent(pauseTitle);
        pauseMenu.addComponent(optionsButton);
        pauseMenu.addComponent(leaveButton);
        pauseMenu.addComponent(returnButton);
        pauseMenu.setVisible(false);
        addComponent(pauseMenu);

        //death menu

        Label deathTitle = new Label(0, TITLE_Y,camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.death.title"), true, true );
        killerName = new Label(0, SUBTITLE_Y, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.death.killer"), true, true);
        lifeLeft = new Label(0, SUBTITLE_Y - 1f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.death.lives"), true, true);
        Label respawnText = new Label(0, SUBTITLE_Y - 3f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.death.respawn"), true, true);
        counter =  new Label(0, SUBTITLE_Y - 4f, camera.getXUnits(), 1, 0.75f, "" + seconds, true, true);

        deathMenu.addComponent(deathTitle);
        deathMenu.addComponent(killerName);
        deathMenu.addComponent(lifeLeft);
        deathMenu.addComponent(respawnText);
        deathMenu.addComponent(counter);
        deathMenu.setVisible(false);
        addComponent(deathMenu);

        //lose menu

        Label loseTitle = new Label(0, TITLE_Y,camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.lose.title"), true, true );
        Button spectateButton = new Button(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT,ResourceHandler.getLanguages().getString("ui.game.button.spectate"));
        Button homeButton =  new Button(BUTTON_X, BUTTON_Y -2, BUTTON_WIDTH, BUTTON_HEIGHT,ResourceHandler.getLanguages().getString("ui.game.button.back"));

        loseMenu.addComponent(loseTitle);
        loseMenu.addComponent(killerName);
        loseMenu.addComponent(spectateButton);
        loseMenu.addComponent(homeButton);
        loseMenu.setVisible(false);
        addComponent(loseMenu);

        //win menu

        winMenu = new Container();
        Label winTitle = new Label(0, TITLE_Y,camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.win.title"), true, true );
        Label winSubtitle = new Label(0, SUBTITLE_Y, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.win.subtitle"), true, true);

        winMenu.addComponent(winTitle);
        winMenu.addComponent(winSubtitle);
        winMenu.addComponent(homeButton);
        winMenu.setVisible(false);
        addComponent(winMenu);
    }

    public void hideScreen() {
        winMenu.setVisible(false);
        loseMenu.setVisible(false);
        deathMenu.setVisible(false);
        pauseMenu.setVisible(false);

    }

    public void showWinScreen() {
        winMenu.setVisible(true);
    }

    public void showLoseScreen(String killer) {
        killerName.setText(ResourceHandler.getLanguages().getString("ui.death.killer").replace("%s", killer));
        loseMenu.setVisible(true);
    }

    public void showDeathScreen(String killer, int lives) {
        killerName.setText(ResourceHandler.getLanguages().getString("ui.death.killer").replace("%s", killer));
        lifeLeft.setText(ResourceHandler.getLanguages().getString("ui.death.lives").replace("%s", lives + ""));
        deathMenu.setVisible(true);
    }

    public void showPauseScreen() {
        pauseMenu.setVisible(true);
    }

    @Override
    public void update(){
        super.update();

        if(Math.floor(Time.getSeconds()) % 4 != seconds) {
            seconds =  ((int) Math.floor(Time.getSeconds())) % 4;
            //if (counter != null) counter.setText(seconds + "");
        }

        if (Globals.getRendering().getFps() != lastFPS){
            fps.setText("" + Globals.getRendering().getFps());
            lastFPS = Globals.getRendering().getFps();
        }
    }
}