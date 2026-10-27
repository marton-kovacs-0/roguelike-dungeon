
package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.decoration.ChestPlacement;
import com.marton.roguelike.world.generation.decoration.ExitPlacement;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

import com.marton.roguelike.world.generation.metadata.RoomBasedMapGenerationResult;
import com.marton.roguelike.world.generation.room.CorridorGeneration;
import com.marton.roguelike.world.generation.room.Room;
import com.marton.roguelike.world.generation.room.RoomGeneration;

import java.util.List;
import java.util.Set;

public class RoomBasedMapGenerator implements MapGenerator {

    private static final int ROOM_COUNT = 20;

    private final RoomGeneration roomGeneration;
    private final CorridorGeneration corridorGeneration;
    private final ChestPlacement chestPlacement;
    private final ExitPlacement exitPlacement;

    public RoomBasedMapGenerator() {
        this.roomGeneration = new RoomGeneration();
        this.corridorGeneration = new CorridorGeneration();
        this.chestPlacement = new ChestPlacement();
        this.exitPlacement = new ExitPlacement();
    }

    @Override
    public MapGenerationResult generate(int width, int height) {
        roomGeneration.clearRooms();
        GameMap gameMap = new GameMap(width, height);

        // Base dungeon
        fillWithWalls(gameMap);

        // Dungeon layout
        while (roomGeneration.getRooms().size() < ROOM_COUNT) {
            roomGeneration.createRoom(gameMap);
        }

        // Generate corridors and keep track of their positions
        Set<TilePosition> corridorPositions =
            corridorGeneration.createCorridors(
                gameMap,
                roomGeneration.getRooms()
            );

        // World population
        chestPlacement.placeChests(roomGeneration.getRooms(), gameMap);

        // Place the dungeon exit on a valid room boundary tile
        exitPlacement.placeExit(
            roomGeneration.getRooms(),
            gameMap,
            corridorPositions
        );

        return new RoomBasedMapGenerationResult(gameMap, roomGeneration.getRooms());
    }

    private void fillWithWalls(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(
                    x,
                    y,
                    new Cell(
                        new TilePosition(x, y),
                        MapCellType.WALL
                    )
                );
            }
        }
    }

    public List<Room> getRooms() {
        return roomGeneration.getRooms();
    }
}
