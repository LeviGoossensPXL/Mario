package components;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import saphire.Component;

public class SpriteRenderer extends Component {

    @Getter
    private Vector4f color;
    private Vector2f[] textCoords;
    @Getter
    private Texture texture;

    public  SpriteRenderer(Vector4f color) {
        this.texture = null;
        this.color = color;
    }

    public  SpriteRenderer(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public Vector2f[] getTextCoords() {
        return new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 1),
                new Vector2f(0, 0),
        };
    }
}
