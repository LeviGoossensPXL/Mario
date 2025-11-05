package components;

import lombok.Getter;
import org.joml.Vector4f;
import saphire.Component;

public class SpriteRenderer extends Component {

    public  SpriteRenderer(Vector4f color) {
        this.color = color;
    }
    @Getter
    private Vector4f color;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }
}
