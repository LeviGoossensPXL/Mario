package saphire;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {
        System.out.println((1.0f / dt) + "FPS");

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.getInstance().r -= dt * 3.0f;
            Window.getInstance().g -= dt * 3.0f;
            Window.getInstance().b -= dt * 3.0f;
        }
        else if (changingScene) {
            Window.changeScene(SceneType.LevelScene);
        }
    }
}
