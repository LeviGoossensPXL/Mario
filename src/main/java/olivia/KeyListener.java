package olivia;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < get().keyPressed.length) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
            }
        } else {
            throw new ArrayIndexOutOfBoundsException("Key is out of array bounds");
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else {
            throw new ArrayIndexOutOfBoundsException("Key is out of array bounds");
        }
    }
}
