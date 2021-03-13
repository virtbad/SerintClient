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
}
