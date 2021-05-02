package ch.virtbad.serint.client.config;

import lombok.Getter;
import lombok.Setter;

/**
 * This class contains all config attributes
 * @author Virt
 */
@Getter @Setter
public class Config {

    private String windowTitle = "Serint Client";
    private int width = 1080;
    private int height = 720;

    private boolean showFps = true;
    private int fps = 60;

    private String serverHost = "localhost";
    private int serverPort = 17371;

    private String language = "en_US";
    private float uiScale = 3.5f;

    private boolean enableAspects = true;
    private boolean enableCosmetics = true;
    private int maxLightSources = 50;
}
