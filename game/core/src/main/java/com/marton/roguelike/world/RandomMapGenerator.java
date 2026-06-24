package com.marton.roguelike.world;

public class RandomMapGenerator {

    private static final double FLOOR_CHANCE = 0.70;
    private static final double WALL_CHANCE = 0.25;

    public GameMap generate(int width, int height) {
        GameMap gameMap = new GameMap(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorderTile(x, y, width, height)) {
                    gameMap.setCell(x, y, new Cell(x, y, CellType.WALL));
                } else {
                    gameMap.setCell(x, y, generateRandomCell(x, y));
                }
            }
        }

        return gameMap;
    }

    private Cell generateRandomCell(int x, int y) {
        double random = Math.random();

        if (random < FLOOR_CHANCE) {
            return new Cell(x, y, CellType.FLOOR);
        }

       if (random < FLOOR_CHANCE + WALL_CHANCE) {
           return new Cell(x, y, CellType.WALL);
       }

       return new Cell(x, y, CellType.TREE);
    }

    private boolean isBorderTile(int x, int y, int width, int height) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

}
