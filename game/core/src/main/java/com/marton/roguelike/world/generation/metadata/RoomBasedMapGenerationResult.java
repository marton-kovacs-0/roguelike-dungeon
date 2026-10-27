package com.marton.roguelike.world.generation.metadata;

import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.generation.room.Room;

import java.util.List;

public class RoomBasedMapGenerationResult implements MapGenerationResult{
    private final GameMap gameMap;
    private final List<Room> rooms;

    public RoomBasedMapGenerationResult(GameMap gameMap, List<Room> rooms) {
        this.gameMap = gameMap;
        this.rooms = List.copyOf(rooms);
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
