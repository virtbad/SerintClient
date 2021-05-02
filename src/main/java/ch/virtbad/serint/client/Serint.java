package ch.virtbad.serint.client;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.graphics.DisplayHandler;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.networking.NetworkHandler;
import ch.virtbad.serint.client.ui.*;
import ch.virtbad.serint.client.util.Time;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Virt
 */
@Slf4j
public class Serint {
    public static final String VERSION = "1.0";

    NetworkHandler network;
    Communications communications;
    DisplayHandler rendering;

    Game game;

    private LoadingScene loading;
    private MainMenu mainMenu;
    private SettingsMenu settingsMenu;
    private AboutMenu aboutMenu;
    private ServerConnectMenu connectMenu;
    private KickScene kickMenu;

    private boolean running;

    /**
     * Creates the Main Class
     */
    public Serint() {
        log.info("Starting Client version {}", VERSION);

        init();
        create();
        post();

        run();
    }

    /**
     * Initializes minor Things
     */
    public void init(){
        Time.getSeconds(); // Initialize Start variable
        log.info("Initializing key components");
        // Load Config
        ConfigHandler.load("config.json");
        ResourceHandler.init();
        // Create and show Display
        rendering = new DisplayHandler(() -> running = false);
        rendering.init();

        // Show loading scene
        loading = new LoadingScene();
        rendering.addScene(0, loading);
        rendering.setScene(0);
        rendering.getUpdater().forceCall();

        // Loading and creating loading text
        ResourceHandler.getShaders().loadShader("/assets/shaders/font.glsl", "font");
        ResourceHandler.getTextures().loadTexture("/assets/textures/font.png", "font");
        loading.addLoadingText();
        rendering.getUpdater().forceCall();
    }

    /**
     * Creates major things
     */
    public void create(){
        log.info("Creating Client components");

        // Loading Resources
        updateLoadingMessage("Loading Resources");
        ResourceHandler.load(); // Needs to have context to work

        // Initializing Game and Communications
        updateLoadingMessage("Preparing Game");

        network = new NetworkHandler();
        communications = new Communications(() -> rendering.setScene(5));

        // Creating GUI
        updateLoadingMessage("Building GUI");

        mainMenu = new MainMenu(() -> running = false);
        rendering.addScene(1, mainMenu);
        settingsMenu = new SettingsMenu();
        rendering.addScene(2, settingsMenu);
        aboutMenu = new AboutMenu();
        rendering.addScene(3, aboutMenu);
        connectMenu = new ServerConnectMenu(network, communications, this::startGame);
        rendering.addScene(4, connectMenu);
        kickMenu = new KickScene(communications);
        rendering.addScene(5, kickMenu);
    }

    /**
     * Cleans up the creation process
     */
    public void post(){
        log.info("Cleaning current Instance");
        updateLoadingMessage("Finishing up");

        rendering.setScene(1);

        log.info("Finished Initialization in {} Seconds!", Time.getSeconds());
    }

    public void run(){
        log.info("Entering loop");
        running = true;
        while (running) {
            rendering.getUpdater().call();
        }
        log.info("Exited loop");

        close();
    }

    public void close(){
        communications.disconnect();
        network.cancelConnection();
    }

    public void startGame(){
        game = new Game(communications);
        rendering.addScene(10, game);
    }

    /**
     * Updates the text shown on the loading screen
     * @param s new text
     */
    private void updateLoadingMessage(String s){
        loading.setLoadingText(s);
        rendering.getUpdater().forceCall();
    }
}
