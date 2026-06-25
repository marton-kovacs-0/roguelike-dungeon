package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.CellType;
import com.marton.roguelike.world.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomBasedMapGenerator implements MapGenerator{

    private final Random random;
    private final List<Room> rooms = new ArrayList<>();
    private static final int ROOM_PADDING = 1;
    private static final int ROOM_COUNT = 20;

    public RoomBasedMapGenerator() {
        this.random = new Random();
    }

    @Override
    public GameMap generate(int width, int height) {
        GameMap gameMap = new GameMap(width, height);
        fillWithWalls(gameMap);

        while (rooms.size() < ROOM_COUNT) {
            createRoom(gameMap);
        }
        createCorridors(gameMap);
        return gameMap;
    }

    private void createRoom(GameMap gameMap) {
        int roomWidth = random.nextInt(5, 16);
        int roomHeight = random.nextInt(2, 10);

        int roomX = random.nextInt(gameMap.getWidth() - roomWidth) + 1;
        int roomY = random.nextInt(gameMap.getHeight() - roomHeight) + 1;

        Room room = new Room(roomX, roomY, roomWidth, roomHeight);

        if (!intersects(room, ROOM_PADDING)) {
            for (int y = roomY; y < roomY + roomHeight; y++) {
                for (int x = roomX; x < roomX + roomWidth; x++) {
                    gameMap.setCell(x, y, new Cell(x, y, CellType.FLOOR));
                }
            }
            rooms.add(room);
        }

    }

    private boolean intersects(Room newRoom, int padding) {
        for (Room existingRoom : rooms) {
            boolean separated =
                newRoom.getRight() < existingRoom.getLeft() - padding
                || newRoom.getLeft() > existingRoom.getRight() + padding
                || newRoom.getBottom() < existingRoom.getTop() - padding
                || newRoom.getTop() > existingRoom.getBottom() + padding;

            if (!separated) {
                return true;
            }
        }
        return false;
    }

    private void fillWithWalls(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(x, y, CellType.WALL));
            }
        }
    }

    private void createCorridors(GameMap gameMap) {
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
                    new Cell(x, currentRoom.getCenterY(), CellType.FLOOR));
            }



            int startY = Math.min(currentRoom.getCenterY(),
                nextRoom.getCenterY());

            int endY = Math.max(currentRoom.getCenterY(),
                nextRoom.getCenterY());

            for (int y = startY; y <= endY; y++) {
                gameMap.setCell(
                    nextRoom.getCenterX(),
                    y,
                    new Cell(nextRoom.getCenterX(), y, CellType.FLOOR));
            }
        }
    }

}
