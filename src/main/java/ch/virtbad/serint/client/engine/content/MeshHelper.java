package ch.virtbad.serint.client.engine.content;

/**
 * This class contains utility methods for creating basic vertices and indices
 * @author Virt
 */
public class MeshHelper {

    /**
     * Creates Vertices for a Quad
     * @param width width of the quad
     * @param height height of the quad
     * @return vertices of the quad
     */
    public static float[] createQuadVertices(float width, float height){
        return new float[]{
                0, 0,
                0, height,
                width, 0,
                width, height
        };
    }

    /**
     * Creates Indices for a Quad
     * @return indices of the quad
     */
    public static int[] createQuadIndices(){
        return new int[] {
                0, 1, 2,
                1, 3, 2
        };
    }


    /**
     * Creates textured Vertices for a Quad
     * @param width width of the quad
     * @param height height of the quad
     * @return vertices of the quad
     */
    public static float[] createTexturedQuadVertices(float width, float height){
        return new float[]{
                0, 0, 0, 1,
                0, height, 0, 0,
                width, 0, 1, 1,
                width, height, 1, 0
        };
    }

    /**
     * Creates vertices that cover the whole screen with uv coordinates, intended for the usage with frame buffers
     * @return vertices
     */
    public static float[] createFramebufferVertices(){
        return new float[]{
                -1, -1,    0, 0,
                -1,  1,    0, 1,
                 1, -1,    1, 0,
                 1,  1,    1, 1
        };
    }
}
