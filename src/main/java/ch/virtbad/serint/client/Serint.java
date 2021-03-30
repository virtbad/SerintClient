package ch.virtbad.serint.client;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.graphics.DisplayHandler;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.networking.NetworkHandler;
import ch.virtbad.serint.client.ui.LoadingScene;
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

    float startTime;

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
        startTime = Time.getSeconds();

        log.info("Initializing key components");
        // Load Config
        ConfigHandler.load("config.json");
        ResourceHandler.init();
        // Create and show Display
        rendering = new DisplayHandler();

        rendering.addScene(0, new LoadingScene());
        rendering.setScene(0);

        rendering.init();
        rendering.getUpdater().forceCall();
    }

    /**
     * Creates major things
     */
    public void create(){
        log.info("Creating Client components");

        // Loading Resources
        ResourceHandler.load(); // Needs to have context to work


        // Creating Networking
        network = new NetworkHandler();
    }

    /**
     * Cleans up the creation process
     */
    public void post(){
        log.info("Cleaning current Instance");

        tryToConnect();

        rendering.addScene(1, game);
        rendering.setScene(1);

        communications.connect();

        log.info("Finished Initialization in {} Seconds!", (startTime - Time.getSeconds()));
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
}
