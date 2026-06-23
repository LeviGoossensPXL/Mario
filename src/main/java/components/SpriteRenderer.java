package components;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import saphire.Component;

public class SpriteRenderer extends Component {

    @Getter
    private Vector4f color;
    private Sprite sprite;

    public SpriteRenderer(Vector4f color) {
        this.sprite = new Sprite(null);
        this.color = color;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTextureCoords() {
        return sprite.getTextureCoords();
    }
}
