package ch.virtbad.serint.client.engine.input;

import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class implements a basic key listener, which keeps track of the states of the keys
 * @author Virt
 */
public class Mouse {

    private boolean[] buttons;

    @Getter
    private int x, y;
    @Getter
    private int wheel;

    public Mouse(){
        buttons = new boolean[8]; // 8, because GLFW does only support eight MouseButtons (see org.lwjgl.glfw.GLFW.GLFW_KEY_LAST)
    }

    /**
     * Binds the callbacks used for mouse operation detection
     * @param window window to bind to
     */
    public void bindCallbacks(long window){
        glfwSetCursorPosCallback(window, this::positionCallback);
        glfwSetMouseButtonCallback(window, this::buttonCallback);
        glfwSetScrollCallback(window, this::wheelCallback);
    }

    /**
     * Callback which is called by GLFW.
     * This callback is used for Button states
     * @see org.lwjgl.glfw.GLFWMouseButtonCallbackI
     */
    private void buttonCallback(long window, int button, int action, int mods){
        if (!(button < 0 || button >= 8)) buttons[button] = action == GLFW_PRESS;
        else System.err.println("Invalid Mouse Event received with following button: " + button);
    }

    /**
     * Callback which is called by GLFW.
     * This callback is used for Mouse Positions
     * @see org.lwjgl.glfw.GLFWCursorPosCallbackI
     */
    private void positionCallback(long window, double xpos, double ypos){
        // Casting to int, because sub pixel precision is not needed
        x = (int) xpos;
        y = (int) ypos;
    }

    /**
     * Callback which is called by GLFW.
     * This callback is used for Mouse Positions
     * @see org.lwjgl.glfw.GLFWScrollCallbackI
     */
    private void wheelCallback(long window, double xoffset, double yoffset){
        // Only using vertical wheel position
        wheel += (int) yoffset;
    }

    /**
     * Returns whether the specified mouse button is pressed.
     * For Button integers, refer to the GLFW class
     * @see org.lwjgl.glfw.GLFW
     * @param button button to check for
     * @return whether button is pressed
     */
    private boolean isDown(int button){
        if (button < 0 || button >= 8) throw new IllegalStateException("Cannot get State of nonexistent Button. (Only up to 8 mouse buttons are supported)");
        else return buttons[button];
    }

    /**
     * Returns whether the specified mouse button is released.
     * This is the exact opposite of isDown
     * @see #isDown(int)
     */
    private boolean isUp(int button){
        return !isDown(button);
    }

}
