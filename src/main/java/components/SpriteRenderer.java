package components;

import saphire.Component;

public class SpriteRenderer extends Component {

    private int timesLooped = 0;

    @Override
    public void start() {
        System.out.println("SpriteRenderer is starting " + this);
    }

    @Override
    public void update(float dt) {
        if (timesLooped < 5) {
            System.out.println("SpriteRenderer is updating " + this);
            timesLooped++;
        }
    }
}
