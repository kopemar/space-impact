package cz.cvut.fel.Model;

import cz.cvut.fel.Sprite;

import java.util.Objects;

public class Positionable {
    private static int counter = 0;
    private int x;
    private int y;
    private int id = 0;
    private int width;
    private int height;
    private Sprite sprite;

    Positionable() {

    }

    Positionable(int x, int y) {
        this.x = x;
        this.y = y;
        counter++;
        this.id = counter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int X, int Y) {
        this.setX(this.getX() + X);
        this.setY(this.getY() + Y);
        if (getSprite() != null) {
           getSprite().updatePosition();
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Positionable that = (Positionable) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }


}
