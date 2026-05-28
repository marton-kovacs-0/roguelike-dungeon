package com.marton.roguelike.world;

public class Cell {
    private int x, y;
    private CellType cellType;


    public Cell(int x, int y, CellType cellType) {
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

    public CellType getCellType() {
        return cellType;
    }
}
