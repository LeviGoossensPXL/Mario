package util;

import components.Spritesheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static void loadShader(String resourceName) {
        File file = new File(resourceName);
        Shader shader = new Shader(resourceName);
        shader.compileAndLink();
        shaders.put(file.getAbsolutePath(), shader);
    }

    public static void loadTexture(String resourceName) {
        File file = new File(resourceName);
        Texture texture = new Texture(resourceName);
        textures.put(file.getAbsolutePath(), texture);
    }

    public static void loadSpritesheet(String resourceName, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        File file = new File(resourceName);
        Texture texture = getTexture(file.getAbsolutePath());
        Spritesheet spritesheet = new Spritesheet(texture, spriteWidth, spriteHeight, numSprites, spacing);
        spritesheets.put(file.getAbsolutePath(), spritesheet);
    }

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        }
        assert false : "Error: shader with key '" + file.getAbsolutePath() + "' is not found or not loaded";
        return null;
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }
        assert false : "Error: texture with key '" + file.getAbsolutePath() + "' is not found or not loaded";
        return null;
    }

    public static Spritesheet getSpritesheet(String resourceName){
        File file = new File(resourceName);
        if (spritesheets.containsKey(file.getAbsolutePath())) {
            return spritesheets.get(file.getAbsolutePath());
        }
        assert false : "Error: spritesheet with key '" + file.getAbsolutePath() + "' is not found or not loaded";
        return null;
    }
}
