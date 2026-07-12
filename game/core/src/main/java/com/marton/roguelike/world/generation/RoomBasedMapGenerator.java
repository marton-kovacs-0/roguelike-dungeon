package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.CellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.decoration.ChestPlacement;
import com.marton.roguelike.world.generation.room.CorridorGeneration;
import com.marton.roguelike.world.generation.room.RoomGeneration;

public class RoomBasedMapGenerator implements MapGenerator{


    private static final int ROOM_COUNT = 20;
    private final RoomGeneration roomGeneration;
    private final CorridorGeneration corridorGeneration;
    private final ChestPlacement chestPlacement;

    public RoomBasedMapGenerator() {
        this.roomGeneration = new RoomGeneration();
        this.corridorGeneration = new CorridorGeneration();
        this.chestPlacement = new ChestPlacement();
    }

    @Override
    public GameMap generate(int width, int height) {
        roomGeneration.clearRooms();
        GameMap gameMap = new GameMap(width, height);

        // Base dungeon
        fillWithWalls(gameMap);

        // Dungeon layout
        while (roomGeneration.getRooms().size() < ROOM_COUNT) {
            roomGeneration.createRoom(gameMap);
        }
        corridorGeneration.createCorridors(gameMap, roomGeneration.getRooms());

        // World population
        chestPlacement.placeChests(roomGeneration.getRooms(), gameMap);
        return gameMap;
    }


    private void fillWithWalls(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new TilePosition(x, y), CellType.WALL));
            }
        }
    }



}
