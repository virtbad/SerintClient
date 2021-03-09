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
    @Getter
    private static final RenderingGlobals rendering = new RenderingGlobals();

    /**
     * Contains globals about networking
     */
    @Getter @Setter
    public static class NetworkingGlobals {
        private String serverName;
        private String serverDescription;
        private String serverVersion;

        private String serverHostname;
        private int serverPort;

        private float serverPing;
    }

    /**
     * Contains globals about rendering
     */
    @Getter @Setter
    public static class RenderingGlobals {
        private int fps;
    }
}
