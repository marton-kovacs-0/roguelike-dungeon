package com.marton.roguelike.entity;

public abstract class Entity {
    private int x;
    private int y;

    public Entity(int x, int y) {
        if (x < 0) throw new IllegalArgumentException("Invalid x coordinate");
        if (y < 0) throw new IllegalArgumentException("Invalid y coordinate");

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
