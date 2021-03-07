package ch.virtbad.serint.client;

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

        // Loading Communications
        Communications.load();
        // Creating Networking
        network = new NetworkHandler();
    }

    /**
     * Cleans up the creation process
     */
    public void post(){
        log.info("Cleaning current Instance");

        network.connect("localhost", 17371);
    }
}
