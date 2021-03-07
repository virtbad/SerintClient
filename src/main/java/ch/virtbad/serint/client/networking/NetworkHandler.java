package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This class handles major networking stuff
 * @author Virt
 */
@Slf4j
public class NetworkHandler {

    private Client client;

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
    public void connect(String hostname, int port){

        log.info("Connecting to server on {}:{}", hostname, port);

        client = new Client(new ProtocolWrapper().getProtocol(), Communications.getInstance());
        try {
            client.connect(hostname, port);
        } catch (IOException e) {
            log.error("Failed to connect to said server!");
            e.printStackTrace();
        }

        Communications.getInstance().setClient(client);
    }
}
