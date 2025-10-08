package components;

import saphire.Component;

public class FontRenderer extends Component {

    private int timesLooped = 0;

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font renderer");
        }
    }

    @Override
    public void update(float dt) {
        if (timesLooped < 5) {
            System.out.println("Fontrenderer is updating " + this);
            timesLooped++;
        }
    }
}
