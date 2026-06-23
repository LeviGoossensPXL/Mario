package saphire;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Renderer;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.renderer = new Renderer();
        this.camera = new Camera(new Vector2f());

        loadResources();
        GameObject obj1 = new GameObject("object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage.png")));
        this.addGameObjectToScene(obj2);
        int xOffset = 800;
        int yOffset = 10;

        float totalWidth = (float) (600 - 10 * 2);
        float totalHeight = (float) (300 - 10 * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);
                GameObject gameObject = new GameObject("objX:" + x + "Y:" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                gameObject.addComponent(new SpriteRenderer(new Vector4f((xPos - xOffset) / totalWidth, (yPos - yOffset) / totalHeight, 1, 1)));
                this.addGameObjectToScene(gameObject);
            }
        }

        GameObject obj3 = new GameObject("paddle 1", new Transform(new Vector2f(400, 400), new Vector2f(20, 100)));
        obj3.addComponent(new SpriteRenderer(new Vector4f(0.7f, 0.7f, 0.7f, 0f)));
        this.addGameObjectToScene(obj3);
    }

    private void loadResources() {
        AssetPool.loadShader("assets/shaders/default.glsl");
        AssetPool.loadTexture("assets/images/testImage.png");
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getName().equals("paddle 1")) {
                System.out.println("hello from paddle 1");
                gameObject.transform.position.y = (500 - MouseListener.getY()) + 100;
                System.out.println(MouseListener.getY());
                System.out.println(gameObject.transform.position.y());
            }
            gameObject.update(dt);
        }
        this.renderer.render();
    }
}
