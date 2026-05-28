package com.marton.roguelike.world;


import java.util.List;

public class GameMap {

    private int width;
    private int height;
    private Cell[][] cells;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) {
        cells[x][y] = cell;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInsideMap(int x, int y) {
        return x >= 0 && x < width
            && y >= 0 && y < height;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
