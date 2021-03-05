package ch.virtbad.serint.client.engine.content;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class represents a Camera, providing matrices to transform into world and view space
 * @author Virt
 */
public class Camera {

    /**
     * This method creates a matrix to scale and translate a model into world space
     * @param rotation rotation to rotate
     * @param xScale scale on the x axis
     * @param yScale scale on the y axis
     * @param x x coordinate
     * @param y y coordinate
     * @param zIndex the zIndex
     * @return matrix to use in shader
     */
    public static Matrix4f createWorldTranslationMatrix(float rotation, float xScale, float yScale, float x, float y, float zIndex){
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.translate(x, y, zIndex);
        matrix.scale(xScale, yScale, 1);
        matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));

        return matrix;
    }

    private Matrix4f viewMatrix;
    private float xScale, yScale;
    private float xPos, yPos;

    private float xMinUnits, yMinUnits;

    /**
     * This creates a camera
     * @param xMinUnits minimum units in width
     * @param yMinUnits minimum units in height
     */
    public Camera(float xMinUnits, float yMinUnits) {
        this.xMinUnits = xMinUnits;
        this.yMinUnits = yMinUnits;

        viewMatrix = new Matrix4f();
        recalculate();
    }

    /**
     * Sets the screen size and recalculates the matrices
     * @param screenWidth screen width
     * @param screenHeight screen height
     */
    public void setScreenSize(int screenWidth, int screenHeight){
        float unitsInHeight, unitsInWidth;

        // Calculates units on greater axis using the ratio
        if (screenHeight < screenWidth){
            unitsInHeight = yMinUnits;
            unitsInWidth = screenWidth / ((float) screenHeight) * yMinUnits;
        } else {
            unitsInWidth = xMinUnits;
            unitsInHeight = screenHeight / ((float) screenWidth) * xMinUnits;
        }

        xScale = 2f / unitsInWidth; // 2 because of NDC -1 to 1
        yScale = 2f / unitsInHeight;

        recalculate();
    }

    /**
     * Sets the supposed world location of the camera, and recalculates the matrices
     * @param x x position
     * @param y y position
     */
    public void setWorldLocation(float x, float y){
        this.xPos = x;
        this.yPos = y;

        recalculate();
    }

    /**
     * Recalculates the view matrix with new translations and scales
     */
    private void recalculate(){
        viewMatrix.identity();

        viewMatrix.translate(-1, -1, 0); // Translate to bottom left corner
        viewMatrix.scale(xScale, yScale, 0); // Scale to desired screen size
        viewMatrix.translate(xPos, yPos, 0); // Translate to world coordinates of camera
    }

    /**
     * This Method returns the view Matrix used to translate in the shader
     * @return matrix to pass to the shader
     */
    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

}
