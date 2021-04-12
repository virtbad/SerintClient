package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.Window;
import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * This class handles the update / drawing thread and the window
 * @author Virt
 */
@Slf4j
public class DisplayHandler {

    private Window window;

    private int selected;
    private HashMap<Integer, Scene> scenes;

    @Getter
    private DisplayUpdater updater;

    private Keyboard keyboard;
    private Mouse mouse;

    /**
     * Creates a display handler
     */
    public DisplayHandler(){
        scenes = new HashMap<>();

        updater = new DisplayUpdater(this);
    }

    /**
     * Starts the rendering thread
     */
    public void init(){
        log.info("Initializing Rendering");

        window = new Window(ConfigHandler.getConfig().getWindowTitle(), ConfigHandler.getConfig().getWidth(), ConfigHandler.getConfig().getHeight());
        // Initializing Window with OpenGL
        window.init();
        window.show();

        window.setResizeEvent(() -> resize(window.getWidth(), window.getHeight()));
        window.setCloseEvent(() -> System.exit(0)); // TODO: Do proper closing procedure

        keyboard = window.getKeyboard();
        mouse = window.getMouse();

        updater.setFrameRate(ConfigHandler.getConfig().getFps());
        updater.forceCall();
    }

    /**
     * Adds a scene to the display
     * @param id if of the scene
     * @param scene scene to add
     */
    public void addScene(int id, Scene scene) {
        log.info("Added scene {}", id);
        scenes.put(id, scene);
        // Initializing Scene
        scenes.get(id).setKeyboard(keyboard);
        scenes.get(id).setMouse(mouse);
        scenes.get(id).init(window.getWidth(), window.getHeight());
    }

    /**
     * Sets a scene to display
     * @param id id of that scene
     */
    public void setScene(int id){
        log.info("Selected scene {}", id);
        selected = id;
    }

    /**
     * Updates / Renders the scene
     */
    public void update() {
        if (scenes.get(selected) != null) {
            scenes.get(selected).update();
            scenes.get(selected).draw();
        }

        window.displayBuffer();
        window.fetchEvents();
    }

    /**
     * Should be called when the window was resized
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        // TODO: Do base resizing
        scenes.get(selected).resized(width, height);
    }

}
