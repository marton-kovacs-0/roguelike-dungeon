package com.marton.roguelike.world.generation.room;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import java.util.List;

public class CorridorGeneration {

    // Connect each room to the next room in the list using an L-shaped corridor. (creation order)
    // The loop stops at the second-to-last room because each iteration accesses
    // the next room with rooms.get(i + 1).
    public void createCorridors(GameMap gameMap, List<Room> rooms) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);

            //horizontal corridor part
            int startX = Math.min(currentRoom.getCenterX(),
                nextRoom.getCenterX());

            int endX = Math.max(currentRoom.getCenterX(),
                nextRoom.getCenterX());


            for (int x = startX; x <= endX; x++) {
                gameMap.setCell(
                    x,
                    currentRoom.getCenterY(),
                    new Cell(new TilePosition(x, currentRoom.getCenterY()), MapCellType.FLOOR));
            }


            //vertical corridor part
            int startY = Math.min(currentRoom.getCenterY(),
                nextRoom.getCenterY());

            int endY = Math.max(currentRoom.getCenterY(),
                nextRoom.getCenterY());

            for (int y = startY; y <= endY; y++) {
                gameMap.setCell(
                    nextRoom.getCenterX(),
                    y,
                    new Cell(new TilePosition(nextRoom.getCenterX(), y), MapCellType.FLOOR));
            }
        }
    }
}
