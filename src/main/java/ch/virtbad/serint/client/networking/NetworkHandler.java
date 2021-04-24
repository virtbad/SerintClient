package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.client.Client;
import ch.virtbad.serint.client.util.Globals;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * This class handles major networking stuff
 * @author Virt
 */
@Slf4j
public class NetworkHandler {
    private Client client;
    private Communications communications;

    private Thread connectionThread;
    private boolean connectionCanceled;

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
    public void connect(Communications communications, String hostname, int port){
        this.communications = communications;
        communications.setStatus(Communications.ESTABLISHING);

        log.info("Connecting to server on {}:{}", hostname, port);

        client = new Client(new ProtocolWrapper().getProtocol(), communications);
        communications.setClient(client);

        startConnection(hostname, port);
    }

    /**
     * Starts the connection in a separate thread
     * @param hostname hostname
     * @param port port
     */
    private void startConnection(String hostname, int port){
        log.info("Starting connection thread");

        if (connectionThread != null && connectionThread.isAlive()) {
            log.warn("Previous thread was still alive");
            connectionThread.interrupt();
        }

        connectionCanceled = false;
        connectionThread = new Thread(() -> {

            log.info("Started connection thread");

            do {
                try {

                    client.connect(hostname, port);
                    if (Thread.currentThread().getId() != connectionThread.getId() || connectionCanceled) break;

                    Globals.getNetwork().setServerHostname(hostname);
                    Globals.getNetwork().setServerPort(port);

                    log.info("Logging into the server");

                    communications.connect();

                } catch (UnknownHostException e) {
                    log.error("Failed to connect to server: " + e.getClass());
                    if (Thread.currentThread().getId() != connectionThread.getId() || connectionCanceled) break;

                    communications.setStatus(Communications.FAILED_ESTABLISH);
                    communications.setStatusInformation("Unknown Host");

                } catch (IOException e) {
                    log.error("Failed to connect to server: " + e.getClass());
                    if (Thread.currentThread().getId() != connectionThread.getId() || connectionCanceled) break;

                    communications.setStatus(Communications.FAILED_ESTABLISH);
                    communications.setStatusInformation(e.getMessage());
                }
            } while (false); // Workaround for it to be able to break out

            log.info("Finished connection thread");

        }, "connection");
        connectionThread.start();

    }

    /**
     * Stops the connection thread
     */
    public void cancelConnection(){
        log.info("Canceling connection");
        // Not sure whether this does sth
        connectionCanceled = true;
        if (connectionThread != null && connectionThread.isAlive()) connectionThread.interrupt();
    }


}
