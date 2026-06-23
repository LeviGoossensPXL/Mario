package components;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import saphire.Component;
import saphire.Transform;

public class SpriteRenderer extends Component {

    @Getter
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    @Getter
    private boolean isDirty = false;

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
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copyTo(this.lastTransform);
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTextureCoords() {
        return sprite.getTextureCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        this.color = color;
        this.isDirty = true;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
