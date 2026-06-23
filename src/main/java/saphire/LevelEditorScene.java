package saphire;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
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
        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj1 = new GameObject("object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        assert spritesheet != null;
        obj1.addComponent(new SpriteRenderer(spritesheet.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(spritesheet.getSprite(10)));
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
    }

    private void loadResources() {
        AssetPool.loadShader("assets/shaders/default.glsl");
        AssetPool.loadTexture("assets/images/testImage.png");
        AssetPool.loadTexture("assets/images/spritesheet.png");
        AssetPool.loadSpritesheet("assets/images/spritesheet.png", 16, 16, 26, 0);
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));
        for (GameObject gameObject : gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
    }
}
