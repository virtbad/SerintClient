package ch.virtbad.serint.client.util;

import lombok.Getter;
import lombok.Setter;

/**
 * This class contains some sort of global variables that are kept updated
 * @author Virt
 */
public class Globals {

    @Getter
    private static final NetworkingGlobals network = new NetworkingGlobals();

    /**
     * Contains globals about networking
     */
    public static class NetworkingGlobals {
        @Getter @Setter private String serverName;
        @Getter @Setter private String serverDescription;

        @Getter @Setter private String serverHostname;
        @Getter @Setter private int serverPort;

        @Getter @Setter private float serverPing;
    }
}
