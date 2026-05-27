package com.marton.roguelike.world;

public class Cell {
    private int x, y;
    private CellType cellType;


    public Cell(int x, int y, CellType cellType, boolean isWalkable) {
        this.x = x;
        this.y = y;
        this.cellType = cellType;
    }

    public String getTileName() {
        return cellType.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWalkable() {
        return cellType.isWalkable();
    }
}
