package com.marton.roguelike.world.generation.decoration;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.CellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.room.Room;

import java.util.List;
import java.util.Random;

public class ChestPlacement {

    private final Random random = new Random();
    private static final double CHEST_CHANCE = 0.35;

    public void placeChests(List<Room> rooms, GameMap gameMap) {
        for (Room room : rooms) {
            if (random.nextDouble() > CHEST_CHANCE) {
                continue;
            }

            List<TilePosition> possiblePlacements = room.getInnerBorderPositions();
            List<TilePosition> validPlacements = possiblePlacements.stream()
                .filter(position -> isValidPosition(gameMap, position))
                .toList();

            if (validPlacements.isEmpty()) {
                continue;
            }

            TilePosition chestPosition = chooseRandomPlacement(validPlacements);
            gameMap.setCell(chestPosition.x(), chestPosition.y(), new Cell(chestPosition, CellType.CLOSED_CHEST));
        }
    }

    // Returns true if the position is adjacent to at least one wall.
    private boolean hasAdjacentWall(GameMap gameMap, TilePosition position) {
        return !gameMap.isWalkable(position.x() - 1, position.y())
            || !gameMap.isWalkable(position.x() + 1, position.y())
            || !gameMap.isWalkable(position.x(), position.y() - 1)
            || !gameMap.isWalkable(position.x(), position.y() + 1);
    }


    private boolean isValidPosition(GameMap gameMap, TilePosition position) {
        return hasAdjacentWall(gameMap, position);
    }

    private TilePosition chooseRandomPlacement(List<TilePosition> positions) {
        return positions.get(random.nextInt(positions.size()));
    }
}
