package ch.virtbad.serint.client.engine.content;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class stores a basic mesh that is loaded into cpu memory.
 *
 * It should be used in the following fashion:
 * On Initialization:
 * init();
 * addVertexAttribPointer();
 * On Rendering:
 * bind Shader;
 * upload Uniforms;
 * draw();
 *
 * @author Virt
 */
public class Mesh {

    private final float[] vertices;
    private final int[] indices;

    private int vbo, ebo, vao;

    /**
     * Creates a Mesh
     * @param vertices vertices - points to draw triangles between
     * @param indices indices - how the vertices are distributed onto triangles
     */
    public Mesh(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    /**
     * Initializes the Mesh. Creates the buffers and uploads the data.
     */
    public void init(){
        // Opening Buffer space
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Binding Buffers onto VAO
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    /**
     * Adds a vertex attribute pointer. Should be called after initialization:
     * @param index index of the pointer
     * @param size size of the pointer in values
     * @param type type of the pointer (see OpenGL constants)
     * @param stride how much bytes long a vertex is
     * @param offset offset to the previous attribute
     */
    public void addVertexAttribPointer(int index, int size, int type, int stride, int offset){
        glVertexAttribPointer(index, size, type, false, stride, offset);
        glEnableVertexAttribArray(index);
    }

    /**
     * Draws the Mesh onto the screen
     */
    public void draw(){
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, NULL);
    }

    /**
     * Removes the stored buffers from gpu memory
     */
    public void destroy(){
        // Make Sure none is binded
        glBindVertexArray(0);

        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);

        glDeleteVertexArrays(vao);
    }
}
