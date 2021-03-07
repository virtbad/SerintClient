package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virtbad.serint.client.util.Globals;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This class handles major networking stuff
 * @author Virt
 */
@Slf4j
public class NetworkHandler {

    private Client client;
    private Communications communications;

    /**
     * Initializes the NetworkHandler and its components
     */
    public NetworkHandler(){

    }

    /**
     * Connects to the server at the listed address
     * @param hostname hostname or ip to connect to
     * @param port port to connect to
     */
    public Communications connect(Communications communications, String hostname, int port){

        log.info("Connecting to server on {}:{}", hostname, port);

        client = new Client(new ProtocolWrapper().getProtocol(), communications);
        try {
            client.connect(hostname, port);
        } catch (IOException e) {
            log.error("Failed to connect to said server!");
            e.printStackTrace();

            return null;
        }

        Globals.getNetwork().setServerHostname(hostname);
        Globals.getNetwork().setServerPort(port);

        communications.setClient(client);

        return communications;
    }
}
