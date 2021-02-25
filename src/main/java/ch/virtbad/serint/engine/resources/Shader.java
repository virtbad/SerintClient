package ch.virtbad.serint.engine.resources;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class stores code for using and creating shaders
 * @author Virt
 */
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
            System.err.println(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
            throw new RuntimeException("Failed to link Shaders");
        }

        // Frees up again space used for primitive Shader storage both on GPU and in Memory
        vertex.delete();
        fragment.delete();
        vertex = null;
        fragment = null;
    }

}
