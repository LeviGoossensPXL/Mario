package saphire;

import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    @Getter(lazy = true)
    private static final KeyListener instance = new KeyListener();

    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {

    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (key <= 0 ||key >= getInstance().keyPressed.length) {
            return; // key int value we cant store
        }
        if (action == GLFW_PRESS) {
            getInstance().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            getInstance().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }

    public static void registerCallbacks(long window) {
        glfwSetKeyCallback(window, KeyListener::keyCallback);
    }
}
