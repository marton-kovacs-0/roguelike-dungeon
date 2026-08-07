package com.marton.roguelike.world;

public enum MapCellType {
    EMPTY("empty", false),
    WALL("wall", false),
    FLOOR("floor", true),
    TREE("tree", false),
    CLOSED_CHEST("closedChest", false);

    private final String tileName;
    private final boolean walkable;

    MapCellType(String tileName, boolean walkable) {
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
