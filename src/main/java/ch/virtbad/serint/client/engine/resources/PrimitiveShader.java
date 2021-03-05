package ch.virtbad.serint.client.engine.resources;

import lombok.Getter;
import static org.lwjgl.opengl.GL20.*;

/**
 * This class defines a primitive part of a shader, this part is usually a Fragment or Vertex shader
 * @author Virt
 */
public class PrimitiveShader {

    @Getter
    private final int id;

    /**
     * This creates and compiles the shader
     * @param source source code of the shader
     * @param type type of the shader, one of GL_FRAGMENT_SHADER or GL_VERTEX_SHADER.
     * @see org.lwjgl.opengl.GL20
     */
    public PrimitiveShader(String source, int type){
        id = glCreateShader(type);

        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println(glGetShaderInfoLog(id, glGetShaderi(id, GL_INFO_LOG_LENGTH)));
            throw new RuntimeException("Failed to compile Shader");
        }
    }

    /**
     * Removes the Shader from GPU Memory
     */
    public void delete(){
        glDeleteShader(id);
    }
}
