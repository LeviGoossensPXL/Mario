package renderer;

import components.SpriteRenderer;
import saphire.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> renderBatches;

    public Renderer() {
        this.renderBatches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null) {
            add(spriteRenderer);
        }
    }

    private void add(SpriteRenderer spriteRenderer) {
        boolean added = false;
        for (RenderBatch renderBatch : renderBatches) {
            if (renderBatch.isHasRoom() && renderBatch.getZIndex() == spriteRenderer.gameObject.getZIndex()) {
                Texture texture = spriteRenderer.getTexture();
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTextureRoom())) { // TODO: might be unneeded because of if statement in add method in Renderer class.
                    renderBatch.addSpriteRenderer(spriteRenderer);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spriteRenderer.gameObject.getZIndex());
            newBatch.start();
            renderBatches.add(newBatch);
            newBatch.addSpriteRenderer(spriteRenderer);
            Collections.sort(renderBatches);
        }
    }

    public void render() {
        for (RenderBatch renderBatch : renderBatches) {
            renderBatch.render();
        }
    }
}
