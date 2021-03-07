package ch.virtbad.serint.client;

import ch.virtbad.serint.client.game.Game;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.networking.NetworkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Virt
 */
@Slf4j
public class Serint {
    public static final String VERSION = "0.1-alpha";

    NetworkHandler network;
    Communications communications;

    Game game;

    /**
     * Creates the Main Class
     */
    public Serint() {
        log.info("Starting Client version {}", VERSION);

        init();
        create();
        post();
    }

    /**
     * Initializes minor Things
     */
    public void init(){
        log.info("Initializing key components");

    }

    /**
     * Creates major things
     */
    public void create(){
        log.info("Creating Client components");

        // Creating Networking
        network = new NetworkHandler();
    }

    /**
     * Cleans up the creation process
     */
    public void post(){
        log.info("Cleaning current Instance");

        tryToConnect();
    }

    public void tryToConnect(){
        communications = network.connect(new Communications(), "localhost", 17371);
        if (communications == null){
            log.error("Failed to connect to server, going to exit.");
            System.exit(0);
        }

        game = new Game(communications);
    }
}
