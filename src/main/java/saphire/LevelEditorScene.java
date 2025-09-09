package saphire;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

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
            // position                // color                     //
            100.0f, 0.0f,   0.0f,       1.0f, 0.0f, 0.0f, 1.0f,     1, 1,  // bottom right     0
            0.0f,   100.0f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,     0, 0,  // top left         1
            100.0f, 100.0f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f,     1, 0,  // top right        2
            0.0f,   0.0f,   0.0f,       1.0f, 1.0f, 0.0f, 1.0f,     0, 1   // bottom left      3
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
            0, 2, 1, //is different form tutorial
            0, 1, 3,
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture; //test texture

    GameObject testObject;
    boolean firstTime = true;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("creating test object 1");
        testObject = new GameObject("test object 1");
        testObject.addComponent(new FontRenderer());
        testObject.addComponent(new SpriteRenderer());
        addGameObjectToScene(this.testObject);

        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compileAndLink();
        this.testTexture = new Texture(new File("assets/images/testImage.png")); //test texture

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
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorsSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorsSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

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
        if (firstTime) {
            System.out.println("creating test object 2");
            GameObject go1 = new GameObject("test object 2");
            go1.addComponent(new SpriteRenderer());
            addGameObjectToScene(go1);
            firstTime = false;
        }


        for (GameObject gameObject: gameObjects) {
            gameObject.update(dt);
        }
    }
}
