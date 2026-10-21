package com.marton.roguelike.world;

public enum MapCellType {
    EMPTY("empty", false, true),
    WALL("wall", false, true),
    FLOOR("floor", true, false),
    TREE("tree", false, true),
    CLOSED_CHEST("closedChest", false, false),
    CLOSED_DOOR("closedDoor", false, true);

    private final String tileName;
    private final boolean walkable;
    private final boolean blocksVision;

    MapCellType(String tileName, boolean walkable, boolean blocksVision) {
        this.tileName = tileName;
        this.walkable = walkable;
        this.blocksVision = blocksVision;
    }

    public String getTileName() {
        return tileName;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean blocksVision() {
        return blocksVision;
    }
}
