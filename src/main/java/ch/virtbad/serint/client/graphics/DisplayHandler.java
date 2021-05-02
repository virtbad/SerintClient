package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.Window;
import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.events.IntegerEvent;
import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class handles the update / drawing thread and the window
 * @author Virt
 */
@Slf4j
public class DisplayHandler {

    private Window window;

    private int last;
    private int selected;
    private HashMap<Integer, Scene> scenes;

    @Getter
    private DisplayUpdater updater;

    private Keyboard keyboard;
    private Mouse mouse;

    private IntegerEvent sceneSwitcher;

    private final BasicEvent close;

    /**
     * Creates a display handler
     * @param close called when it is time to close the game
     */
    public DisplayHandler(BasicEvent close){
        this.close = close;
        scenes = new HashMap<>();

        updater = new DisplayUpdater(this);

        sceneSwitcher = this::setScene;
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
        window.setCloseEvent(close);

        keyboard = window.getKeyboard();
        mouse = window.getMouse();

        log.info("Running on GPU: \"{}\" by \"{}\" ", glGetString(GL_RENDERER), glGetString(GL_VENDOR));

        log.info("Framerate set on {}fps", ConfigHandler.getConfig().getFps());
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
        scenes.get(id).setSceneSwitcher(sceneSwitcher);
    }

    /**
     * Sets a scene to display
     * @param id id of that scene (-1 for previous)
     */
    public void setScene(int id){
        log.info("Selected scene {}", id);
        if(id == -1){ // -1 Corresponds to last Scene
            int current = selected;
            selected = last;
            last = current;
        }else {
            last = selected;
            selected = id;
        }

        if (scenes.get(selected) != null){
            scenes.get(selected).resized(window.getWidth(), window.getHeight());
            scenes.get(selected).shown();
        }
    }

    /**
     * Updates / Renders the scene
     */
    public void update() {
        if (scenes.get(selected) != null) {
            scenes.get(selected).update();
            scenes.get(selected).draw();
        }

        glFinish(); // Tells opengl to finish its operation before continuing. Seems to improve the smoothness

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
