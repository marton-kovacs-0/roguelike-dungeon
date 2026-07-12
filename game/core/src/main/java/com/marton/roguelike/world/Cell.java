package com.marton.roguelike.world;

public class Cell {
    private final TilePosition position;
    private CellType cellType;


    public Cell(TilePosition position, CellType cellType) {
        this.position = position;
        this.cellType = cellType;
    }

    public String getTileName() {
        return cellType.getTileName();
    }

    public TilePosition getPosition() {
        return position;
    }

    public CellType getCellType() {
        return cellType;
    }
}
