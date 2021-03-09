package ch.virtbad.serint.client.engine;


import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.events.BooleanEvent;
import ch.virtbad.serint.client.engine.events.EventHelper;
import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class provides easy to use methods, to interact with the GLFW Window
 * @author Virt
 */
public class Window {

    private long id;

    @Getter
    private int width, height;

    private String name;

    @Setter
    private BasicEvent closeEvent, resizeEvent;
    @Setter
    private BooleanEvent focusEvent;

    public Window(String name, int width, int height){
        this.name = name;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes the window using GLFW and then Creates it
     */
    public void init(){
        // Initialize GLFW
        if (!glfwInit()) throw new UnsupportedOperationException("Your operating system seems not to support GLFW, or GLFW failed otherwise to initialize.");

        // Specifying that we want to use OpenGL 3.3 core profile
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create Window
        id = glfwCreateWindow(width, height, name, NULL, NULL);
        if (id == 0) throw new RuntimeException("Failed to create Window!");

        glfwMakeContextCurrent(id);

        // Initialize OpenGL
        GL.createCapabilities();
        GL11.glViewport(0, 0, width, height);
        glEnable(GL_BLEND); // Configure Transparency
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // Add Callbacks
        glfwSetWindowCloseCallback(id, window -> EventHelper.emitEvent(closeEvent));
        glfwSetWindowFocusCallback(id, (window, focused) -> EventHelper.emitEvent(focusEvent, focused));
        glfwSetFramebufferSizeCallback(id, (window, w, h) -> {
            height = h;
            width = w;

            GL11.glViewport(0, 0, w, h);

            EventHelper.emitEvent(resizeEvent);
        });
    }

    /**
     * Shows the Window to the User
     */
    public void show(){
        glfwShowWindow(id);
    }

    /**
     * Displays new Frame in buffer
     */
    public void displayBuffer(){
        glfwSwapBuffers(id);
    }

    /**
     * Fetches events from the system. NEEDS TO BE CALLED FROM MAIN THREAD!
     */
    public void fetchEvents(){
        glfwPollEvents();
    }

    /**
     * Closes the Window
     */
    public void close(){
        glfwTerminate();
    }

    /**
     * Creates a keyboard listener for the window
     * @return Keyboard listener
     */
    public Keyboard getKeyboard(){
        Keyboard keyboard = new Keyboard();
        keyboard.bindCallback(this.id);
        return keyboard;
    }

    /**
     * Creates a mouse listener for the window
     * @return Mouse Listener
     */
    public Mouse getMouse(){
        Mouse mouse = new Mouse();
        mouse.bindCallbacks(this.id);
        return mouse;
    }

    /**
     * Gives the context to the current thread
     */
    public void obtainContext(){
        glfwMakeContextCurrent(id);
        GL.createCapabilities();
    }

    /**
     * Removes the current Context
     */
    public void loseContext(){
        glfwMakeContextCurrent(NULL);
    }
}
