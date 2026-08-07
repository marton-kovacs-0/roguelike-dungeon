package com.marton.roguelike.world;

import com.marton.roguelike.item.WorldItem;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private final int width;
    private final int height;
    private final Cell[][] cells;
    private final List<WorldItem> worldItems;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        this.worldItems = new ArrayList<>();
    }

    // ---------------------------------------------------------------------
    // TilePosition-based API
    // ---------------------------------------------------------------------

    public Cell getCell(TilePosition position) {
        return getCell(position.x(), position.y());
    }

    public void setCell(TilePosition position, Cell cell) {
        setCell(position.x(), position.y(), cell);
    }

    public boolean isInsideMap(TilePosition position) {
        return isInsideMap(position.x(), position.y());
    }

    public boolean isWalkable(TilePosition position) {
        return isWalkable(position.x(), position.y());
    }

    public List<WorldItem> getWorldItems() {
        return List.copyOf(worldItems);
    }

    public void addWorldItem(WorldItem item) {
        worldItems.add(item);
    }

    public void removeWorldItem(WorldItem item) {
        worldItems.remove(item);
    }

    // ---------------------------------------------------------------------
    // Raw tile coordinate API
    // ---------------------------------------------------------------------

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    public boolean isInsideMap(int x, int y) {
        return x >= 0 && x < width
            && y >= 0 && y < height;
    }

    public boolean isWalkable(int x, int y) {
        if (!isInsideMap(x, y)) {
            return false;
        }

        return getCell(x, y)
            .getCellType()
            .isWalkable();
    }

    // ---------------------------------------------------------------------
    // Map properties
    // ---------------------------------------------------------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getCells() {
        return cells;
    }

    // ---------------------------------------------------------------------
    // Utility methods
    // ---------------------------------------------------------------------

    public Cell getRandomWalkableCell() {
        List<Cell> walkables = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = getCell(x, y);

                if (cell.getCellType().isWalkable()) {
                    walkables.add(cell);
                }
            }
        }

        return walkables.get((int) (Math.random() * walkables.size()));
    }
}
