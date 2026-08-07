package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

public class RandomMapGenerator implements MapGenerator {

    private static final double FLOOR_CHANCE = 0.70;
    private static final double WALL_CHANCE = 0.10;
    private static final double TREE_CHANCE = 0.18;

    @Override
    public GameMap generate(int width, int height) {
        GameMap gameMap = new GameMap(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorderTile(x, y, width, height)) {
                    gameMap.setCell(x, y, new Cell(new TilePosition(x, y), MapCellType.WALL));
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
            return new Cell(new TilePosition(x, y), MapCellType.FLOOR);
        }

        if (random < FLOOR_CHANCE + WALL_CHANCE) {
            return new Cell(new TilePosition(x, y), MapCellType.WALL);
        }

        if (random < FLOOR_CHANCE + WALL_CHANCE + TREE_CHANCE) {
            return new Cell(new TilePosition(x, y), MapCellType.TREE);
        }

        return new Cell(new TilePosition(x, y), MapCellType.EMPTY);
    }

    private boolean isBorderTile(int x, int y, int width, int height) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

}
