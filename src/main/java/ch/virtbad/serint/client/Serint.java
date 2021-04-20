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
    public static final String VERSION = "0.8-beta";

    NetworkHandler network;
    Communications communications;
    DisplayHandler rendering;

    Game game;

    private LoadingScene loading;
    private MainMenu mainMenu;
    private SettingsMenu settingsMenu;
    private AboutMenu aboutMenu;
    private ServerConnectMenu connectMenu;

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
        rendering = new DisplayHandler();
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

        // Creating Networking
        updateLoadingMessage("Initializing Networking");
        network = new NetworkHandler();
    }

    /**
     * Cleans up the creation process
     */
    public void post(){
        log.info("Cleaning current Instance");
        updateLoadingMessage("Finishing up");

        mainMenu = new MainMenu();
        rendering.addScene(1, mainMenu);
        settingsMenu = new SettingsMenu();
        rendering.addScene(2, settingsMenu);
        aboutMenu = new AboutMenu();
        rendering.addScene(3, aboutMenu);
        connectMenu = new ServerConnectMenu();
        rendering.addScene(4, connectMenu);

        if (true){
            tryToConnect();

            rendering.addScene(10, game);

            communications.connect();

            rendering.setScene(10);
        }else {
            rendering.setScene(1);
        }


        log.info("Finished Initialization in {} Seconds!", Time.getSeconds());
    }

    public void run(){
        while (true) { // TODO: Add breakpoint(s)
            rendering.getUpdater().call();
        }
    }

    public void tryToConnect(){
        communications = network.connect(new Communications(), ConfigHandler.getConfig().getServerHost(), ConfigHandler.getConfig().getServerPort());
        if (communications == null){
            log.error("Failed to connect to server, going to exit.");
            System.exit(0);
        }

        game = new Game(communications);
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
