package ch.virtbad.serint.client.engine.input;

import lombok.extern.slf4j.Slf4j;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class implements a basic key listener, which keeps track of the states of the keys
 * @author Virt
 */
@Slf4j
public class Keyboard {
    private boolean[] keys;

    public Keyboard(){
        keys = new boolean[350]; // Roughly 350, because the highest keycode is about that (see org.lwjgl.glfw.GLFW.GLFW_KEY_LAST)
    }

    /**
     * Binds the callback used for key detection to the window
     * @param windowId id to bind callback to
     */
    public void bindCallback(long windowId){
        glfwSetKeyCallback(windowId, this::callback);
    }

    /**
     * Callback which is getting called by GLFW
     * @see org.lwjgl.glfw.GLFWKeyCallbackI
     */
    private void callback(long window, int key, int scancode, int action, int mods){
        if (!(key < 0 || key >= 350)) keys[key] = action != GLFW_RELEASE; // False if key was released, so GLFW_PRESS and GLFW_REPEAT both are true
        else log.warn("Invalid Keyboard Event received with following key: " + key);
    }

    /**
     * Returns whether the specified key is down.
     * For key integers, refer to the GLFW class
     * @see org.lwjgl.glfw.GLFW
     * @param key key to check
     * @return whether key is down
     */
    public boolean isDown(int key){
        if (key < 0 || key >= 350) throw new IllegalStateException("Cannot get State of nonexistent Key");
        return keys[key];
    }

    /**
     * Returns whether the key is up.
     * This is the exact opposite of isDown
     * @see #isDown(int)
     */
    public boolean isUp(int key){
        return !isDown(key);
    }
}
