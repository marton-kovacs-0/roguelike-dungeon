package com.marton.roguelike.world;

public enum CellType {
    EMPTY("empty", false),
    WALL("wall", false),
    FLOOR("floor", true);

    private final String tileName;
    private final boolean walkable;

    CellType(String tileName, boolean walkable) {
        this.tileName = tileName;
        this.walkable = walkable;
    }

    public String getTileName() {
        return tileName;
    }

    public boolean isWalkable() {
        return walkable;
    }
}
