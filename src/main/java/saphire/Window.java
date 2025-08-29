package saphire;

import lombok.Getter;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    @Getter(lazy = true)
    private static final Window instance = new Window();

    private static Scene currentScene = new LevelEditorScene();

    private String title;
    private int width, height;
    private long glfwWindow;

    public float r = 1, g = 1, b = 1, a = 1;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario Maker";
    }

    public static void changeScene(SceneType newScene) {
        switch (newScene) {
            case SceneType.LevelEditorScene:
                currentScene = new LevelEditorScene();
                break;
            case SceneType.LevelScene:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene: " + newScene;
                break;
        }
        System.out.println("Current scene: " + currentScene.toString());
    }

    public void run() {
        System.out.printf("Hello LWJGL %s!", Version.getVersion());

        init();
        loop();

        //cleanup glfw
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        //free error callback
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        //setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        //config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);


        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        MouseListener.registerCallbacks(glfwWindow);
        KeyListener.registerCallbacks(glfwWindow);

        //make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //no wait time between frames
        glfwSwapInterval(1);


        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float deltaTime = endTime - beginTime;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime; //to capture any lag between here and beginning of this loop
        }

    }
}
