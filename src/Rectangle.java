import processing.core.PApplet;
import processing.core.PVector;

import static java.lang.Float.max;
import static processing.core.PApplet.min;

public class Rectangle {

    private float x;
    private float y;
    private float width;
    private float height;

    public Rectangle(float x, float y, float width, float height) {
        this.set(x, y, width, height);
    }

    public Rectangle(float width, float height) {
        this(0, 0, width, height);
    }

    public Rectangle(PVector corner1, PVector corner2) {
        this.set(corner1, corner2);
    }

    public Rectangle(Rectangle r) {
        this.set(r);
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void set(PVector corner1, PVector corner2) {
        float x = min(corner1.x, corner2.x);
        float y = min(corner1.y, corner2.y);
        float width = max(corner1.x, corner2.x) - x;
        float height = max(corner1.y, corner2.y) - y;
        this.set(x, y, width, height);
    }

    public void set(Rectangle r) {
        this.set(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setPosition(PVector position) {
        setPosition(position.x, position.y);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void display(PApplet applet) {
        applet.rect(x, y, width, height);
    }

    public boolean contains(float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    public boolean contains(PVector pVector) {
        return contains(pVector.x, pVector.y);
    }

    public boolean overlaps(Rectangle r) {
        return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
    }

}
