
package com.marton.roguelike.world.generation.map;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.metadata.BasicMapGenerationResult;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

import java.util.*;

public class CellularAutomataMapGenerator implements MapGenerator {

    // Minimum dimensions required for a usable cave layout. (at least 3x3)
    private static final int MINIMUM_MAP_SIZE = 10;

    private static final double FLOOR_CHANCE = 0.43;
    private static final int ITERATIONS = 4;
    private static final int WALL_THRESHOLD = 5;
    private final Random random = new Random();

    // floors are false, walls are true values
    @Override
    public MapGenerationResult generate(int width, int height) {
        if (width < MINIMUM_MAP_SIZE || height < MINIMUM_MAP_SIZE) {
            throw new IllegalArgumentException("The map is too small for this generation type");
        }

        boolean[][] currentGrid = createNoise(width, height);

        for (int i = 0; i < ITERATIONS; i++) {
            currentGrid = smoothGrid(width, height, currentGrid);
        }

        GameMap gameMap = new GameMap(width, height);

        for (int x = 0; x < currentGrid.length; x++) {
            for (int y = 0; y < currentGrid[x].length; y++) {
                TilePosition position = new TilePosition(x, y);

                MapCellType cellType = currentGrid[x][y]
                    ? MapCellType.WALL
                    : MapCellType.FLOOR;

                Cell cell = new Cell(position, cellType);
                gameMap.setCell(position, cell);
            }
        }

        keepLargestFloorRegion(gameMap);
        placeExit(gameMap);

        return new BasicMapGenerationResult(gameMap);
    }


    private boolean[][] createNoise(int width, int height) {
        boolean[][] grid = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1
                    || y == 0 || y == height - 1) {
                    grid[x][y] = true;
                } else {
                    grid[x][y] = random.nextDouble() > FLOOR_CHANCE;
                }
            }
        }

        return grid;
    }


    /* The generate() method must ensure that this method only gets a position
    where it does not check cells outside the grid */
    private int countWallNeighbors(boolean[][] grid, int x, int y) {
        int count = 0;

        for (int dx = x - 1; dx < x + 2; dx++) {
            for (int dy = y - 1; dy < y + 2; dy++) {
                if (dx == x && dy == y) continue;
                if (grid[dx][dy]) count++;
            }
        }

        return count;
    }

    private boolean[][] smoothGrid(int width, int height, boolean[][] currentGrid) {
        boolean[][] result = new boolean[width][height];

        // Copy the top and bottom boundaries.
        for (int x = 0; x < width; x++) {
            result[x][0] = currentGrid[x][0];
            result[x][height - 1] = currentGrid[x][height - 1];
        }

        // Copy the left and right boundaries.
        for (int y = 0; y < height; y++) {
            result[0][y] = currentGrid[0][y];
            result[width - 1][y] = currentGrid[width - 1][y];
        }

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                result[x][y] =
                    countWallNeighbors(currentGrid, x, y) >= WALL_THRESHOLD;
            }
        }

        return result;
    }

    private void placeExit(GameMap gameMap) {
        List<TilePosition> candidates = new ArrayList<>();

        for (int x = 1; x < gameMap.getWidth() - 1; x++) {
            for (int y = 1; y < gameMap.getHeight() - 1; y++) {
                Cell cell = gameMap.getCell(x, y);

                if (cell.getCellType() != MapCellType.FLOOR) {
                    continue;
                }

                if (hasAdjacentWall(gameMap, x, y)) {
                    candidates.add(cell.getPosition());
                }
            }
        }

        if (candidates.isEmpty()) {
            throw new IllegalStateException(
                "Cannot place an exit: no suitable floor cell was found."
            );
        }

        TilePosition exitPosition =
            candidates.get(random.nextInt(candidates.size()));

        gameMap.setCell(
            exitPosition,
            new Cell(exitPosition, MapCellType.CLOSED_DOOR)
        );
    }

    private boolean hasAdjacentWall(GameMap gameMap, int x, int y) {
        return gameMap.getCell(x - 1, y).getCellType() == MapCellType.WALL
            || gameMap.getCell(x + 1, y).getCellType() == MapCellType.WALL
            || gameMap.getCell(x, y - 1).getCellType() == MapCellType.WALL
            || gameMap.getCell(x, y + 1).getCellType() == MapCellType.WALL;
    }

    private void keepLargestFloorRegion(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        boolean[][] visited = new boolean[width][height];
        List<TilePosition> largestRegion = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (visited[x][y]) {
                    continue;
                }

                if (gameMap.getCell(x, y).getCellType() != MapCellType.FLOOR) {
                    continue;
                }

                List<TilePosition> region =
                    findFloorRegion(gameMap, x, y, visited);

                if (region.size() > largestRegion.size()) {
                    largestRegion = region;
                }
            }
        }

        if (largestRegion.isEmpty()) {
            throw new IllegalStateException(
                "Cannot generate a map without floor cells."
            );
        }

        boolean[][] keep = new boolean[width][height];

        for (TilePosition position : largestRegion) {
            keep[position.x()][position.y()] = true;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gameMap.getCell(x, y).getCellType() == MapCellType.FLOOR
                    && !keep[x][y]) {

                    TilePosition position = new TilePosition(x, y);
                    gameMap.setCell(
                        position,
                        new Cell(position, MapCellType.WALL)
                    );
                }
            }
        }
    }

    private List<TilePosition> findFloorRegion(
        GameMap gameMap,
        int startX,
        int startY,
        boolean[][] visited
    ) {
        List<TilePosition> region = new ArrayList<>();
        Queue<TilePosition> queue = new ArrayDeque<>();

        TilePosition start = new TilePosition(startX, startY);

        queue.add(start);
        visited[startX][startY] = true;

        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        while (!queue.isEmpty()) {
            TilePosition current = queue.poll();
            region.add(current);

            for (int[] direction : directions) {
                int nextX = current.x() + direction[0];
                int nextY = current.y() + direction[1];

                if (nextX < 0 || nextX >= gameMap.getWidth()
                    || nextY < 0 || nextY >= gameMap.getHeight()) {
                    continue;
                }

                if (visited[nextX][nextY]) {
                    continue;
                }

                if (gameMap.getCell(nextX, nextY).getCellType()
                    != MapCellType.FLOOR) {
                    continue;
                }

                visited[nextX][nextY] = true;
                queue.add(new TilePosition(nextX, nextY));
            }
        }

        return region;
    }
}
