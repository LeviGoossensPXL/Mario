package saphire;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    private void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copyTo(Transform targetTransform) {
        targetTransform.position.set(this.position);
        targetTransform.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
