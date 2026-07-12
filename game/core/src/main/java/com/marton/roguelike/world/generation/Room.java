package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.TilePosition;

public class Room {

    private final TilePosition position;
    private final int width;
    private final int height;

    public Room(int x, int y, int width, int height) {
        this.position = new TilePosition(x, y);
        this.width = width;
        this.height = height;
    }

    public int getLeft() {
        return position.x();
    }

    public int getRight() {
        return position.x() + width - 1;
    }

    public int getTop() {
        return position.y();
    }

    public int getBottom() {
        return position.y() + height - 1;
    }

    public int getCenterX() {
        return position.x() + width / 2;
    }

    public int getCenterY() {
        return position.y() + height / 2;
    }
}
