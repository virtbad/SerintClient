package ch.virtbad.serint.client.config;

import lombok.Getter;

/**
 * This class contains all config attributes
 * @author Virt
 */
@Getter
public class Config {

    private String windowTitle = "Serint Client";
    private int width = 1080;
    private int height = 720;

    private int fps = 60;

}
