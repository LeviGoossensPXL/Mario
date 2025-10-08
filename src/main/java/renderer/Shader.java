package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            vertexSource = getShaderString(source, GL_VERTEX_SHADER);
            fragmentSource = getShaderString(source, GL_FRAGMENT_SHADER);
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: could not open shader file: '" + filepath + "'";
        }

    }

    public void compileAndLink() {
        int vertexID = compileShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentID = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
        shaderProgramID = linkShader(vertexID, fragmentID);
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    // region upload methods
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec3.x, vec3.y, vec3.z);
    }

    public void uploadVec4f(String varName, Vector2f vec2) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec2.x, vec2.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int textureID) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, textureID);
    }
    // endregion

    private String getShaderString(String source, int shaderType) {
        String[] split = source.split("(//type)( )+([a-zA-Z]+)");

        int start = 0;
        int end = 0;

        for (int i = 1; i < split.length; i++) { // skip first string in split array
            start = source.indexOf("//type", end) + "//type ".length();
            end = source.indexOf(System.lineSeparator(), start);
            String pattern = source.substring(start, end).trim();

            if (pattern.equals("vertex") && shaderType == GL_VERTEX_SHADER) {
                return split[i];
            } else if (pattern.equals("fragment") && shaderType == GL_FRAGMENT_SHADER) {
                return split[i];
            }
        }
        return null;
    }

    private int compileShader(int shaderType, String shaderSource) {
        // compile and link shaders
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        // Check for compilation errors
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);

            if (shaderType == GL_VERTEX_SHADER) {
                System.err.println("ERROR: '" + filepath + "'\nVertex shader failed to compile.");
            }
            if (shaderType == GL_FRAGMENT_SHADER) {
                System.err.println("ERROR: '" + filepath + "'\nFragment shader failed to compile.");
            }
            System.err.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }
        return shaderID;
    }

    private int linkShader(int vertexId, int fragmentId) {
        // link shaders
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filepath + "'\nShader linking failed.");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        return shaderProgram;
    }
}
