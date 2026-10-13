
package com.marton.roguelike.world.generation.decoration;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.room.Room;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ExitPlacement {

    private final Random random = new Random();

    public void placeExit(
        List<Room> rooms,
        GameMap gameMap,
        Set<TilePosition> corridorPositions
    ) {
        // Collect all valid positions along the inner room borders.
        List<TilePosition> validPositions = rooms.stream()
            .flatMap(room -> room.getInnerBorderPositions().stream())
            .filter(position ->
                isValidPosition(gameMap, position, corridorPositions)
            )
            .toList();

        if (validPositions.isEmpty()) {
            throw new IllegalStateException(
                "Could not find a valid position for the dungeon exit."
            );
        }

        // Randomly select one valid position for the exit.
        TilePosition exitPosition = validPositions.get(
            random.nextInt(validPositions.size())
        );

        gameMap.setCell(
            exitPosition,
            new Cell(exitPosition, MapCellType.CLOSED_DOOR)
        );
    }

    private boolean isValidPosition(
        GameMap gameMap,
        TilePosition position,
        Set<TilePosition> corridorPositions
    ) {
        // The exit must be placed on a walkable floor tile.
        if (!gameMap.isWalkable(position)) {
            return false;
        }

        // Never place the exit on a corridor tile.
        if (corridorPositions.contains(position)) {
            return false;
        }

        // Exclude positions occupied by chests.
        if (gameMap.getCell(position).getCellType()
            == MapCellType.CLOSED_CHEST) {
            return false;
        }

        // The exit must be directly next to a wall.
        return hasAdjacentWall(gameMap, position);
    }

    private boolean hasAdjacentWall(
        GameMap gameMap,
        TilePosition position
    ) {
        return isWall(gameMap, position.x() - 1, position.y())
            || isWall(gameMap, position.x() + 1, position.y())
            || isWall(gameMap, position.x(), position.y() - 1)
            || isWall(gameMap, position.x(), position.y() + 1);
    }

    private boolean isWall(GameMap gameMap, int x, int y) {
        return gameMap.isInsideMap(x, y)
            && gameMap.getCell(x, y).getCellType()
            == MapCellType.WALL;
    }
}
