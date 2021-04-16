package ch.virtbad.serint.client.ui;

/**
 * This class handles basic operation useful for ui components
 * @autor Virt
 */
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

}
