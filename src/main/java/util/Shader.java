package util;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    public static int createShader(int shaderType, String shaderSource){
        // compile and link shaders
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        // Check for compilation errors
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);

            if (shaderType == GL_VERTEX_SHADER) {
                System.err.println("ERROR: 'default.glsl'\nVertex shader failed to compile.");
            }
            if (shaderType == GL_FRAGMENT_SHADER) {
                System.err.println("ERROR: 'default.glsl'\nFragment shader failed to compile.");
            }
            System.err.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }
        return shaderID;
    }

    public static int linkShader(int vertexId, int fragmentId) {
        // link shaders
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: 'default.glsl'\nShader linking failed.");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        return shaderProgram;
    }
}
