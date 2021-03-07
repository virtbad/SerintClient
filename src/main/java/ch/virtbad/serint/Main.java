package ch.virtbad.serint;

import ch.virtbad.serint.client.Serint;
import lombok.extern.slf4j.Slf4j;

/**
 * This class only wraps the main method and may parse the options
 * @author Virt
 */
@Slf4j
public class Main {

    /**
     * Main Function - Entry Point into program
     * @param args console args
     */
    public static void main(String[] args) {
        log.info("Starting Serint");

        new Serint(); // Just evoking the constructor

        log.info("Terminated Starting Thread");
    }
}
