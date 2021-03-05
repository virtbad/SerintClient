package ch.virtbad.serint.client.engine.resources;

import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class stores code for using and creating shaders
 * @author Virt
 */
@Slf4j
public class Shader {
    public static final String TYPE_INDICATOR = "#type ";
    public static final String TYPE_VERTEX = "vertex";
    public static final String TYPE_FRAGMENT = "fragment";

    private int id;

    private PrimitiveShader vertex, fragment;

    /**
     * Creates a shader
     * @param source source of shader, both containing vertex and fragment shader
     */
    public Shader(String source){
        // Extracts the single Shaders out of the big file
        String[] shaders = source.split(TYPE_INDICATOR);

        String vertexSource = null, fragmentSource = null;

        for (String shader : shaders) {
            shader = shader.trim();

            if (shader.startsWith(TYPE_VERTEX)) {

                vertexSource = shader.substring(TYPE_VERTEX.length());

            }else if (shader.startsWith(TYPE_FRAGMENT)) {

                fragmentSource = shader.substring(TYPE_FRAGMENT.length());

            }
        }

        if (fragmentSource == null || vertexSource == null) throw new RuntimeException("Complete Shader file does not contain both types of shader!");

        load(vertexSource, fragmentSource);
    }

    /**
     * Creates a shader
     * @param vertexSource source of the vertex Shader
     * @param fragmentSource source of the fragment Shader
     */
    public Shader(String vertexSource, String fragmentSource){
        load(vertexSource, fragmentSource);
    }

    /**
     * Compiles links and loads a shader onto the GPU
     * @param vertexSource source of the vertex Shader
     * @param fragmentSource source of the fragment Shader
     */
    private void load(String vertexSource, String fragmentSource){
        // Initializes the primitive Shaders
        vertex = new PrimitiveShader(vertexSource, GL_VERTEX_SHADER);
        fragment = new PrimitiveShader(fragmentSource, GL_FRAGMENT_SHADER);

        // Creates Program
        id = glCreateProgram();

        glAttachShader(id, vertex.getId());
        glAttachShader(id, fragment.getId());

        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE){
            log.error("Failed to link Shaders");
            log.error(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
            throw new RuntimeException("Failed to link Shaders");
        }

        // Frees up again space used for primitive Shader storage both on GPU and in Memory
        vertex.delete();
        fragment.delete();
        vertex = null;
        fragment = null;
    }

    /**
     * Binds a shader for usage
     */
    public void bind(){
        glUseProgram(id);
    }

    /**
     * Uploads a 4x4 Matrix as a uniform
     * @param name name of the uniform
     * @param mat matrix to upload
     */
    public void uploadMatrix4f(String name, Matrix4f mat){
        int loc = glGetUniformLocation(id, name);

        // Stores matrix in float array
        float[] floats = new float[4 * 4];
        mat.get(floats);

        glUniformMatrix4fv(loc, false, floats);
    }

}
