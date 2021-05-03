package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.Context;
import ch.virtbad.serint.client.ui.MenuScene;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.game.player.Player;
import ch.virtbad.serint.client.game.ui.HudIndicator;
import ch.virtbad.serint.client.game.ui.KillIndicator;
import ch.virtbad.serint.client.ui.UiScene;
import ch.virtbad.serint.client.ui.components.Container;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.util.Time;
import lombok.Setter;
import org.joml.Vector4f;

import static ch.virtbad.serint.client.ui.MainMenu.*;


/**
 * Contains the ui that is shown in game
 * @author Virt
 */
public class GameUI extends MenuScene {

    private Camera hudCamera;
    private Context hudContext;
    private int lastFPS;
    private Label fps;

    private Container hudContainer;
    HudIndicator hud;
    KillIndicator kill;


    private Container pauseMenu;
    private Container deathMenu;
    private Label counter;
    private Label killerName;
    private Label lifeLeft;
    private Container loseMenu;
    private Container winMenu;
    private Container startMenu;
    private Label startCountdown;

    private float countdownStart;
    private float countdown = 0;
    boolean menuChanged = false;
    private String issuer = "";

    @Setter
    private boolean started;
    private Player own;

    private final BasicEvent disconnect;
    private final BasicEvent backToGame;

    public GameUI(BasicEvent disconnect, BasicEvent backToGame) {
        this.disconnect = disconnect;
        this.backToGame = backToGame;
    }

    @Override
    public void init(int width, int height) {

        hudCamera = new Camera(20, 20);
        hudCamera.setScreenSize(width, height);

        hudContext = new Context(hudCamera, keyboard, mouse);

        super.init(width, height);
    }

    @Override
    public void build() {

        // Avoid Black overlay
        background = new Vector4f(0, 0, 0, 0);

        // FPS Counter
        fps = new Label(hudCamera.getXUnits() - 0.1f, hudCamera.getYUnits() - 0.5f - 0.1f, 10, 0.5f, 0.5f, "-", false, true);
        fps.setContext(hudContext);
        fps.init();

        // Ingame Hud
        hudContainer = new Container();
        hudContainer.setContext(hudContext);
        hudContainer.init();

        kill = new KillIndicator(hudCamera.getXUnits() - 3 - 0.5f, 0.5f, 3, 3);
        hudContainer.addComponent(kill);
      
        hud = new HudIndicator(0.5f, hudCamera.getYUnits() - 3.5f, 9, 3);
        hudContainer.addComponent(hud);

        // Menus
        pauseMenu = new Container();
        deathMenu = new Container();
        loseMenu = new Container();
        winMenu = new Container();

        // Pause menu

        Label pauseTitle = new Label(-camera.getXUnits() / 2, BUTTON_HEIGHT, camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.game.pause.title"), true, false );
        Button leaveButton =  new Button(-BUTTON_WIDTH / 2, pauseTitle.getY() - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.game.pause.leave"));
        Button returnButton =  new Button(-BUTTON_WIDTH / 2, leaveButton.getY() - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.game.pause.back"));
        returnButton.setEvent(backToGame);
        leaveButton.setEvent(disconnect);

        pauseMenu.addComponent(pauseTitle);
        pauseMenu.addComponent(leaveButton);
        pauseMenu.addComponent(returnButton);
        pauseMenu.setVisible(false);
        addComponent(pauseMenu);

        // Death menu

        Label deathTitle = new Label(-camera.getXUnits() / 2, BUTTON_HEIGHT, camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.game.death.title"), true, false );
        killerName = new Label(-camera.getXUnits() / 2, 1f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.game.death.killer"), true, true);
        lifeLeft = new Label(-camera.getXUnits() / 2, killerName.getY() - 2f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.game.death.lives"), true, true);
        Label respawnText = new Label(-camera.getXUnits() / 2, lifeLeft.getY() - 1f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.game.death.respawn"), true, true);
        counter =  new Label(-camera.getXUnits() / 2, respawnText.getY() - 2f, camera.getXUnits(), 1, 0.75f, "" + 0, true, true);

        deathMenu.addComponent(deathTitle);
        deathMenu.addComponent(killerName);
        deathMenu.addComponent(lifeLeft);
        deathMenu.addComponent(respawnText);
        deathMenu.addComponent(counter);
        deathMenu.setVisible(false);
        addComponent(deathMenu);

        // Lose menu

        Label loseTitle = new Label(-camera.getXUnits() / 2, BUTTON_HEIGHT ,camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.game.lose.title"), true, true );
        Button homeButton =  new Button(-BUTTON_WIDTH / 2, killerName.getY() - BUTTON_SPACING - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.game.finish.back"));
        homeButton.setEvent(disconnect);

        loseMenu.addComponent(loseTitle);
        loseMenu.addComponent(killerName);
        loseMenu.addComponent(homeButton);
        loseMenu.setVisible(false);
        addComponent(loseMenu);

        // Win menu

        winMenu = new Container();
        Label winTitle = new Label(-camera.getXUnits() / 2, BUTTON_HEIGHT, camera.getXUnits(), 1, 1f, ResourceHandler.getLanguages().getString("ui.game.win.title"), true, true );
        Label winSubtitle = new Label(-camera.getXUnits() / 2, 1f, camera.getXUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.game.win.subtitle"), true, true);

        winMenu.addComponent(winTitle);
        winMenu.addComponent(winSubtitle);
        winMenu.addComponent(homeButton);
        winMenu.setVisible(false);
        addComponent(winMenu);

        // Start Menu
        startMenu = new Container();
        Label startTitle = new Label(-camera.getXMinUnits() / 2, -camera.getYMinUnits() / 2 + BUTTON_SPACING + 1 + BUTTON_SPACING, camera.getXMinUnits(), 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.game.start.title"), true, false);
        startCountdown = new Label(-camera.getXMinUnits() / 2, -camera.getYMinUnits() / 2 + BUTTON_SPACING, camera.getXMinUnits(), 1, 0.75f, "-", true, true);

        startMenu.addComponent(startTitle);
        startMenu.addComponent(startCountdown);
        startMenu.setVisible(false);
        addComponent(startMenu);
    }

    public void hideScreen() {
        winMenu.setVisible(false);
        loseMenu.setVisible(false);
        deathMenu.setVisible(false);
        pauseMenu.setVisible(false);
        startMenu.setVisible(false);

        if (started) hudContainer.setVisible(true);
        else hudContainer.setVisible(false);
    }

    public void showWinScreen() {
        winMenu.setVisible(true);
        hudContainer.setVisible(false);
        pauseMenu.setVisible(false);
    }

    public void showLoseScreen(String killer) {
        menuChanged = true;
        issuer = killer;

        loseMenu.setVisible(true);
        hudContainer.setVisible(false);
        pauseMenu.setVisible(false);
    }

    public void showDeathScreen(String killer, float delay) {
        menuChanged = true;

        countdown = 0;
        countdownStart = Time.getSeconds() + delay;
        issuer = killer;

        deathMenu.setVisible(true);
        hudContainer.setVisible(false);
        pauseMenu.setVisible(false);
    }

    public void showPauseScreen() {
        pauseMenu.setVisible(true);
        hudContainer.setVisible(false);
    }

    public void showStartScreen(float delay){
        countdown = 0;
        countdownStart = Time.getSeconds() + delay;

        startMenu.setVisible(true);
    }

    public void hideHud(){
        hudContainer.setVisible(false);
    }

    public boolean isPaused(){
        return pauseMenu.isVisible();
    }

    @Override
    public void draw(){
        super.draw();

        if (hudContainer.isVisible()) {
            hudContainer.draw();
        }

        if (ConfigHandler.getConfig().isShowFps()) fps.draw();
    }

    @Override
    public void update(){
        super.update();

        if (deathMenu.isVisible()) {
            if (menuChanged){
                menuChanged = false;

                killerName.setText(String.format(ResourceHandler.getLanguages().getString("ui.game.death.killer"), issuer));
                lifeLeft.setText(String.format(ResourceHandler.getLanguages().getString("ui.game.death.lives"), own.getAttributes().getHealth()));
            }

            int time = (int) (Time.getSeconds() * 10 - countdownStart * 10);
            if (time / 10f != countdown) {
                countdown = time / 10f;
                counter.setText("" + (countdown < 0 ? -countdown : 0));
            }
        }else if (loseMenu.isVisible()){
            if (menuChanged) {

                killerName.setText(String.format(ResourceHandler.getLanguages().getString("ui.game.death.killer"), issuer));
            }
        }else if (startMenu.isVisible()){

            int time = (int) (Time.getSeconds() * 10 - countdownStart * 10);
            if (time / 10f != countdown) {
                countdown = time / 10f;
                startCountdown.setText("" + (countdown < 0 ? -countdown : 0));
            }
        }

        if (hudContainer.isVisible()) {
            hudContainer.update(0);
        }

        if (ConfigHandler.getConfig().isShowFps()) {

            if (Globals.getRendering().getFps() != lastFPS) {
                fps.setText("" + Globals.getRendering().getFps());
                fps.setPosition(hudCamera.getXUnits() - fps.getTextWidth() - 0.1f, hudCamera.getYUnits() - 0.5f - 0.1f);
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
        own = player;
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

        hudCamera.setScreenSize(width, height);

        fps.setPosition(hudCamera.getXUnits() - fps.getTextWidth() - 0.1f, hudCamera.getYUnits() - 0.5f - 0.1f);
        hud.setPosition(0.5f, hudCamera.getYUnits() - 3.5f);
        kill.setPosition(hudCamera.getXUnits() - 3 - 0.5f, 0.5f);
    }
}
