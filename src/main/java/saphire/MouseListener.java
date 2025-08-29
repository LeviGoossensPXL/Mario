package saphire;

import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    @Getter(lazy = true)
    private static final MouseListener instance = new MouseListener();

    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;

    private boolean mouseButtonPressed[] = new boolean[3];
    @Getter
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static void mousePositionCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        getInstance().isDragging = getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[1] || getInstance().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = true;
            }
        }
        else if (action == GLFW_RELEASE) {
            if (button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void registerCallbacks(long window) {
        glfwSetCursorPosCallback(window, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
    }

    /**
     * reset variables at the end of the frame
     */
    public static void endFrame() {

        getInstance().scrollX = 0.0;
        getInstance().scrollY = 0.0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX() {
        return (float)getInstance().xPos;
    }

    public static float getY() {
        return (float)getInstance().yPos;
    }

    public static float getDx() {
        return (float)(getInstance().lastX - getInstance().xPos);
    }

    public static float getDy() {
        return (float)(getInstance().lastY - getInstance().yPos);
    }

    public static float getScrollX() {
        return (float)getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float)getInstance().scrollY;
    }

    //isDragging with lombok.@Getter

    public static boolean isMouseButtonDown(int button) {
        if (button >= getInstance().mouseButtonPressed.length) {
            return false;
        }
        return getInstance().mouseButtonPressed[button];
    }
}
