package com.marton.roguelike.entity;

public abstract class Entity {
    private float x;
    private float y;
    private static final float MOVE_SPEED = 5f;
    private static final float WIDTH = 0.8f;
    private static final float HEIGHT = 0.8f;

    public Entity(float x, float y) {
        if (x < 0) throw new IllegalArgumentException("Invalid x coordinate");
        if (y < 0) throw new IllegalArgumentException("Invalid y coordinate");

        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public float getMoveSpeed() {
        return MOVE_SPEED;
    }

    public float getWidth() {
        return WIDTH;
    }

    public float getHeight() {
        return HEIGHT;
    }
}
