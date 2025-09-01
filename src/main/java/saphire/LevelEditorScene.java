package saphire;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {
    float dx = 50.0f;
    float dy = 50.0f;

    private String vertexShaderSrc = "#version 460 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 460 core\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            // position             // color
            100.0f, 0.0f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // bottom right     0
            -0.0f, 100.0f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // top left         1
            100.0f, 100.0f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // top right        2
            0.0f, 0.0f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, // bottom left      3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*  vertex array order
                    x1      x2


                    x3      x0


            counter-clockwise order
                    x3      x2


                    x       x1

                    x2      x


                    x3      x1
             */
            0, 2, 1,
            0, 1, 3,
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compileAndLink();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);


        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorsSize = 4;
        int floatsSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorsSize) * floatsSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatsSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {

        camera.position.x -= dt * dx;
        camera.position.y -= dt * dy;
        if (camera.position.x < -1180) {
            dx = -50;
        }
        if (camera.position.y < -572) {
            dy = -50;
        }
        if (camera.position.x > 0) {
            dx = 50;
        }
        if (camera.position.y > 0) {
            dy = 50;
        }

        System.out.println("camera position: " + camera.position.x + ", " + camera.position.y);
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        glBindVertexArray(vaoID);

        //glEnableVertexAttribArray(0); // seemingly needed in tutorial, seemingly unneeded according to chatGPT and running program
        //glEnableVertexAttribArray(1); // seemingly needed in tutorial, seemingly unneeded according to chatGPT and running program

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //glDisableVertexAttribArray(0); // seemingly needed in tutorial, seemingly unneeded according to chatGPT and running program
        //glDisableVertexAttribArray(1); // seemingly needed in tutorial, seemingly unneeded according to chatGPT and running program

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
