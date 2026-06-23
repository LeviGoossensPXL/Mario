package components;

import lombok.Getter;
import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {
    @Getter
    private Texture texture;
    @Getter
    private Vector2f[] textureCoords;

    public Sprite(Texture texture) {
        this.texture = texture;
        textureCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
        };
    }

    public Sprite(Texture texture, Vector2f[] textureCoords) {
        this.texture = texture;
        this.textureCoords = textureCoords;
    }
}
