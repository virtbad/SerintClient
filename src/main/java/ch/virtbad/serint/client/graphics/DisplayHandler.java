package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.config.Config;
import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.Window;
import ch.virtbad.serint.client.util.Time;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.GL11;

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

    private UpdateThread thread;
    private volatile boolean needToClaimContext = true;
    private volatile boolean getRidOfContext = false;

    /**
     * Creates a display handler
     */
    public DisplayHandler(){
        scenes = new HashMap<>();

        thread = new UpdateThread(this);
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
        window.loseContext();

        needToClaimContext = true;

        thread.setFrameRate(ConfigHandler.getConfig().getFps());
        thread.start();

        while (needToClaimContext); // Wait until context is claimed
    }

    /**
     * Adds a scene to the display
     * @param id if of the scene
     * @param scene scene to add
     */
    public void addScene(int id, Scene scene) {
        log.info("Added scene {}", id);
        scenes.put(id, scene);
        scenes.get(id).init();
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

        if (needToClaimContext){
            log.info("Reclaiming GL Context!");
            window.obtainContext();
            needToClaimContext = false;
        }
        if (getRidOfContext){
            log.info("Getting Rid of GL Context!");
            window.loseContext();
            getRidOfContext = false;
        }

        GL11.glClearColor(1, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        //scenes.get(selected).update();
        //scenes.get(selected).draw();

        window.displayBuffer();
    }

    /**
     * Puts the Thread into the event listening loop
     */
    public void endUpInListeningLoop(){
        log.info("Dedicating Main thread to Event Listening");
        while (thread.isRunning()) window.fetchEvents();
    }

    /**
     * Obtains the GL context into the current thread
     */
    public void obtainContext(){
        log.info("Passing Context onto {}", Thread.currentThread().getName());

        getRidOfContext = true;

        while (getRidOfContext);
        thread.setPaused(true);

        window.obtainContext();
    }

    /**
     * Loses the GL context off of the current thread
     */
    public void loseContext(){
        log.info("Loosing Context");

        needToClaimContext = true;
        window.loseContext();

        thread.setPaused(false);
    }

}
