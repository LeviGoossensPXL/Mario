import olivia.Window;
import renderer.Shader;

public class Main {
    public static void main(String[] args) {
        new Shader("C:\\Users\\levig\\IdeaProjects\\Mario\\assets\\shaders\\default.glsl");
        Window window = Window.get();
        window.run();
    }
}
