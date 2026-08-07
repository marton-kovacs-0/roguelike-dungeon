package com.marton.roguelike.world;

public class Cell {
    private final TilePosition position;
    private MapCellType cellType;


    public Cell(TilePosition position, MapCellType cellType) {
        this.position = position;
        this.cellType = cellType;
    }

    public String getTileName() {
        return cellType.getTileName();
    }

    public TilePosition getPosition() {
        return position;
    }

    public MapCellType getCellType() {
        return cellType;
    }
}
