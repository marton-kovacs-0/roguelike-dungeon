package com.marton.roguelike.world.generation.room;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CorridorGeneration {

    // Connect each room to the next room in the list using an L-shaped corridor. (creation order)
    // The loop stops at the second-to-last room because each iteration accesses
    // the next room with rooms.get(i + 1).
    public Set<TilePosition> createCorridors(GameMap gameMap, List<Room> rooms) {
        Set<TilePosition> corridorPositions = new HashSet<>();

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);

            int startX = Math.min(
                currentRoom.getCenterX(),
                nextRoom.getCenterX()
            );
            int endX = Math.max(
                currentRoom.getCenterX(),
                nextRoom.getCenterX()
            );

            for (int x = startX; x <= endX; x++) {
                setCorridorFloor(
                    gameMap,
                    new TilePosition(x, currentRoom.getCenterY()),
                    corridorPositions
                );
            }

            int startY = Math.min(
                currentRoom.getCenterY(),
                nextRoom.getCenterY()
            );
            int endY = Math.max(
                currentRoom.getCenterY(),
                nextRoom.getCenterY()
            );

            for (int y = startY; y <= endY; y++) {
                setCorridorFloor(
                    gameMap,
                    new TilePosition(nextRoom.getCenterX(), y),
                    corridorPositions
                );
            }
        }

        return Set.copyOf(corridorPositions);
    }

    private void setCorridorFloor(
        GameMap gameMap,
        TilePosition position,
        Set<TilePosition> corridorPositions) {
        gameMap.setCell(position, new Cell(position, MapCellType.FLOOR));
        corridorPositions.add(position);
    }
}
