package saphire;

import org.lwjgl.BufferUtils;
import util.Shader;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#type vertex" +
            "#version 330 core\n" +
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

    private String fragmentShaderSrc = "#type fragment\n" +
            "#version 330 core\n" +
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
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // bottom right     0
            -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // top left         1
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // top right        2
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, // bottom left      3
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
//            0, 2, 1,
//            0, 1, 3,
            2, 1, 0,
            0, 1, 3,
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        vertexID = Shader.createShader(GL_VERTEX_SHADER, vertexShaderSrc);
        fragmentID = Shader.createShader(GL_FRAGMENT_SHADER, fragmentShaderSrc);
        shaderProgram = Shader.linkShader(vertexID, fragmentID);


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
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0); // causes crash: crashlog below
        //# A fatal error has been detected by the Java Runtime Environment:
        //#
        //#  EXCEPTION_ACCESS_VIOLATION (0xc0000005) at pc=0x00007ffd0aee8f0a, pid=24456, tid=35932
        //#
        //# JRE version: OpenJDK Runtime Environment (24.0.2+12) (build 24.0.2+12-54)
        //# Java VM: OpenJDK 64-Bit Server VM (24.0.2+12-54, mixed mode, sharing, tiered, compressed oops, compressed class ptrs, g1 gc, windows-amd64)
        //# Problematic frame:
        //# C  [nvoglv64.dll+0x918f0a]
        //#
        //# No core dump will be written. Minidumps are not enabled by default on client versions of Windows
        //#
        //# An error report file with more information is saved as:
        //# D:\Programming\java\Mario\hs_err_pid24456.log
        //[0.701s][warning][os] Loading hsdis library failed
        //#
        //# If you would like to submit a bug report, please visit:
        //#   https://bugreport.java.com/bugreport/crash.jsp
        //# The crash happened outside the Java Virtual Machine in native code.
        //# See problematic frame for where to report the bug.
        //#
        //2 actionable tasks: 1 executed, 1 up-to-date

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}
