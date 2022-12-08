package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private final String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            // Find all occurrences of substring [#type 'pattern']
            int match = 1;
            while (match < splitString.length) {
                int index = source.indexOf("#type") + "#type".length();
                int eol = source.indexOf("\r\n", index);
                String pattern = source.substring(index, eol).trim();
                switch (pattern) {
                    case "vertex":
                        if (vertexSource == null) {
                            vertexSource = splitString[match].trim();
                            match += 1;
                        } else {
                            throw new IOException("More then one of type: '" + pattern + "' defined in'" + filepath + "'");
                        }
                    case "fragment":
                        if (fragmentSource == null) {
                            fragmentSource = splitString[match].trim();
                            match += 1;
                        } else {
                            throw new IOException("More then one of type: '" + pattern + "' defined in'" + filepath + "'");
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compileAndLink() {
        // ==================================================================================================================
        // Compile and link shaders
        // ==================================================================================================================
        int vertexID, fragmentID;

        // Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for compilation errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for compilation errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        // Unbind shader program
        glUseProgram(0);
    }
}
