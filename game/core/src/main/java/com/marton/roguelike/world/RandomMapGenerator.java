package com.marton.roguelike.world;


import java.util.List;

public class RandomMapGenerator {


    public GameMap generate(int width, int height) {
        GameMap gameMap = new GameMap(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                gameMap.setCell(x, y, generateRandomCell(x, y));
            }
        }

        return gameMap;
    }

    private Cell generateRandomCell(int x, int y) {
        List<CellType> cellTypes = List.of(CellType.FLOOR, CellType.WALL, CellType.TREE);
        return new Cell(x, y, cellTypes.get((int) (Math.random() * 3)));
    }

}
