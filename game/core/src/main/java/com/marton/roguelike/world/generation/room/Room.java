package com.marton.roguelike.world.generation.room;

import com.marton.roguelike.world.TilePosition;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final TilePosition position;
    private final int width;
    private final int height;

    public Room(TilePosition position, int width, int height) {
        this.position = position;
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

    public List<TilePosition> getInnerBorderPositions() {
        List<TilePosition> positions = new ArrayList<>();

        for (int x = getLeft(); x <= getRight(); x++) {
            positions.add(new TilePosition(x, getTop()));
        }

        for (int x = getLeft(); x <= getRight(); x++) {
            positions.add(new TilePosition(x, getBottom()));
        }

        for (int y = getTop() + 1; y < getBottom(); y++) {
            positions.add(new TilePosition(getLeft(), y));
        }

        for (int y = getTop() + 1; y < getBottom(); y++) {
            positions.add(new TilePosition(getRight(), y));
        }

        return positions;
    }

    // Returns every floor position inside the room.
    public List<TilePosition> getFloorPositions() {
        List<TilePosition> positions = new ArrayList<>();

        for (int y = getTop(); y <= getBottom(); y++) {
            for (int x = getLeft(); x <= getRight(); x++) {
                positions.add(new TilePosition(x, y));
            }
        }
        return positions;
    }
}
