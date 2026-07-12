package com.marton.roguelike.world.generation.room;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.CellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomGeneration {
    private final List<Room> rooms;
    private final Random random;
    private static final int ROOM_PADDING = 1;
    private static final int ROOM_MIN_WIDTH = 5;
    private static final int ROOM_MAX_WIDTH = 16;
    private static final int ROOM_MIN_HEIGHT = 2;
    private static final int ROOM_MAX_HEIGHT = 10;

    public RoomGeneration() {
        this.rooms = new ArrayList<>();
        this.random = new Random();
    }

    public List<Room> getRooms() {
        return List.copyOf(rooms);
    }

    public void createRoom(GameMap gameMap) {
        int roomWidth = random.nextInt(ROOM_MIN_WIDTH, ROOM_MAX_WIDTH);
        int roomHeight = random.nextInt(ROOM_MIN_HEIGHT, ROOM_MAX_HEIGHT);

        int roomX = random.nextInt(gameMap.getWidth() - roomWidth) + 1;
        int roomY = random.nextInt(gameMap.getHeight() - roomHeight) + 1;

        Room room = new Room(new TilePosition(roomX, roomY), roomWidth, roomHeight);

        if (!intersects(room, ROOM_PADDING)) {
            for (int y = roomY; y < roomY + roomHeight; y++) {
                for (int x = roomX; x < roomX + roomWidth; x++) {
                    gameMap.setCell(x, y, new Cell(new TilePosition(x, y), CellType.FLOOR));
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

    public void clearRooms() {
        rooms.clear();
    }
}
