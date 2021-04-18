package ch.virtbad.serint.client.ui;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class handles basic operation useful for ui components
 * @author Virt
 */
@Slf4j
public class UiHelper {

    /**
     * Returns whether the mouse hovers a certain portion of the screen
     * @param x x start coordinate of the portion
     * @param y y start coordinate of the portion
     * @param width width of the portion
     * @param height height of the portion
     * @param context context to find mouse and camera
     * @return whether the mouse cursor is hovering
     */
    public static boolean mouseHovering(float x, float y, float width, float height, Context context){
        float mouseX = context.getCamera().screenToWorldPositionX(context.getMouse().getX());
        float mouseY = context.getCamera().screenToWorldPositionY(context.getMouse().getY());

        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    /**
     * Opens a link in a browser
     * @param link link to open
     */
    public static void openLink(String link){
        log.info("Opening Link: {}", link);
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (IOException | URISyntaxException e) {
            log.error("Failed to open that link");
        }
    }

}
