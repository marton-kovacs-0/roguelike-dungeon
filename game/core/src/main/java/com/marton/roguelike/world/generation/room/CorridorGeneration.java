package com.marton.roguelike.world.generation.room;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import java.util.List;

public class CorridorGeneration {

    public void createCorridors(GameMap gameMap, List<Room> rooms) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);

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
