package com.marton.roguelike.world.generation;

public class Room {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getLeft() {
        return x;
    }

    public int getRight() {
        return x + width - 1;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return y + height - 1;
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }
}
