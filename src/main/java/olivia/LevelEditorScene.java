package olivia;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private final String vertexShaderSrc = """
            #version 330 core
            layout (location=0) in vec3 aPos;
            layout (location=1) in vec4 aColor;

            out vec4 fColor;

            void main(){
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0);
            }""";

    private final String fragmentShaderSrc = """
            #version 330 core

            in vec4 fColor;

            out vec4 color;

            void main(){
                color = fColor;
            }""";

    private int vertexID, fragmentID, shaderProgram;

    private final float[] vertexArray = {
            // position             // color
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Top Right    2
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elementArray = {
            /*
                x       x


                x       x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // ==================================================================================================================
        // Compile and link shaders
        // ==================================================================================================================

        // Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for compilation errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to the GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for compilation errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // ==================================================================================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ==================================================================================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // Bind shader program
        glUseProgram(shaderProgram);
        //Bind the VAO that is used
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}
