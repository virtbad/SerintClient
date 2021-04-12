package ch.virtbad.serint.client.engine.resources.shaders;

import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
    public void uploadMatrix(String name, Matrix4f mat){
        int loc = glGetUniformLocation(id, name);

        // Stores matrix in float array
        float[] floats = new float[4 * 4];
        mat.get(floats);

        glUniformMatrix4fv(loc, false, floats);
    }

    /**
     * Uploads a Float as a uniform
     * @param name name of the uniform
     * @param f float to upload
     */
    public void uploadFloat(String name, float f){
        int loc = glGetUniformLocation(id, name);

        glUniform1f(loc, f);
    }

    /**
     * Uploads an Integer as a uniform
     * @param name name of the uniform
     * @param i integer to upload
     */
    public void uploadInt(String name, int i){
        int loc = glGetUniformLocation(id, name);

        glUniform1i(loc, i);
    }

    /**
     * Uploads a Vector with the size 4 as a uniform
     * @param name name of the uniform
     * @param vec vector to upload
     */
    public void uploadVec4(String name, Vector4f vec){
        int loc = glGetUniformLocation(id, name);

        glUniform4f(loc, vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Uploads a Vector with the size 3 as a uniform
     * @param name name of the uniform
     * @param vec vector to upload
     */
    public void uploadVec3(String name, Vector3f vec){
        int loc = glGetUniformLocation(id, name);

        glUniform3f(loc, vec.x, vec.y, vec.z);
    }

    /**
     * Uploads a Vector with the size 2 as a uniform
     * @param name name of the uniform
     * @param vec vector to upload
     */
    public void uploadVec2(String name, Vector2f vec){
        int loc = glGetUniformLocation(id, name);

        glUniform2f(loc, vec.x, vec.y);
    }

    /**
     * Uploads an array of integers as a uniform
     * @param name name of the uniform
     * @param is integers to upload
     */
    public void uploadIntArray(String name, int[] is){
        int loc = glGetUniformLocation(id, name);

        glUniform1iv(loc, is);
    }

    /**
     * Uploads an array of floats as a uniform
     * @param name name of the uniform
     * @param fs floats to upload
     */
    public void uploadFloatArray(String name, float[] fs){
        int loc = glGetUniformLocation(id, name);

        glUniform1fv(loc, fs);
    }

    /**
     * Uploads an array of vectors with the size 2 as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec2Array(String name, Vector2f[] vecs){
        float[] array = new float[vecs.length * 2];
        for (int i = 0; i < vecs.length; i++) {
            array[i * 2] = vecs[i].x;
            array[i * 2 + 1] = vecs[i].y;
        }

        uploadVec2Array(name, array);
    }

    /**
     * Uploads an array of vectors with the size 2 as floats as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec2Array(String name, float[] vecs){
        int loc = glGetUniformLocation(id, name);

        glUniform2fv(loc, vecs);
    }

    /**
     * Uploads an array of vectors with the size 3 as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec3Array(String name, Vector3f[] vecs){
        float[] array = new float[vecs.length * 3];
        for (int i = 0; i < vecs.length; i++) {
            array[i * 3] = vecs[i].x;
            array[i * 3 + 1] = vecs[i].y;
            array[i * 3 + 2] = vecs[i].z;
        }

        uploadVec3Array(name, array);
    }

    /**
     * Uploads an array of vectors with the size 3 as floats as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec3Array(String name, float[] vecs){
        int loc = glGetUniformLocation(id, name);

        glUniform3fv(loc, vecs);
    }

    /**
     * Uploads an array of vectors with the size 4 as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec4Array(String name, Vector4f[] vecs){
        float[] array = new float[vecs.length * 4];
        for (int i = 0; i < vecs.length; i++) {
            array[i * 4] = vecs[i].x;
            array[i * 4 + 1] = vecs[i].y;
            array[i * 4 + 2] = vecs[i].z;
            array[i * 4 + 3] = vecs[i].w;
        }

        uploadVec4Array(name, array);
    }

    /**
     * Uploads an array of vectors with the size 4 as floats as a uniform
     * @param name name of the uniform
     * @param vecs vectors to upload
     */
    public void uploadVec4Array(String name, float[] vecs){
        int loc = glGetUniformLocation(id, name);

        glUniform4fv(loc, vecs);
    }

}
