package com.marton.roguelike.world.generation.enemy.strategy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;
import com.marton.roguelike.world.generation.metadata.RoomBasedMapGenerationResult;
import com.marton.roguelike.world.generation.room.Room;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomBasedEnemySpawnStrategy implements EnemySpawnStrategy{

    private final Random random = new Random();


    @Override
    public List<TilePosition> selectSpawnPositions(
        MapGenerationResult generationResult,
        TilePosition playerPosition,
        int enemyCountTarget,
        int minimumDistance) {

        if (!(generationResult instanceof RoomBasedMapGenerationResult)) {
            throw new IllegalArgumentException(
                "RoomBasedEnemySpawnStrategy requires RoomBasedMapGenerationResult"
            );
        }

        if (enemyCountTarget <= 0) {
            return List.of();
        }

        RoomBasedMapGenerationResult genResult =
            (RoomBasedMapGenerationResult) generationResult;

        List<Room> rooms = genResult.getRooms();
        List<TilePosition> result = new ArrayList<>();

        while (result.size() < enemyCountTarget) {
            int elementsInResult = result.size();

            for (Room room : rooms) {
                List<TilePosition> positionsInRoom = getValidSpawnPositions(
                    room,
                    genResult.getGameMap(),
                    playerPosition,
                    minimumDistance
                );

                // Previously selected positions cannot be selected again.
                positionsInRoom.removeAll(result);

                if (!positionsInRoom.isEmpty()) {
                    TilePosition position = positionsInRoom.get(
                        random.nextInt(positionsInRoom.size())
                    );

                    result.add(position);
                }

                if (result.size() >= enemyCountTarget) {
                    break;
                }
            }

            // Stop when a complete round adds no new positions.
            if (elementsInResult == result.size()) {
                break;
            }
        }

        return result;
    }


    private List<TilePosition> getValidSpawnPositions(
        Room room,
        GameMap gameMap,
        TilePosition playerPosition,
        int minimumDistance) {

        List<TilePosition> result = new ArrayList<>();

        for (TilePosition position : room.getFloorPositions()) {
            if (gameMap.isInsideMap(position) &&
                gameMap.isWalkable(position) &&
                distanceTo(position, playerPosition) >= minimumDistance) {

                result.add(position);
            }
        }

        return result;
    }

    private int distanceTo(TilePosition currentPosition, TilePosition playerPosition) {
        int dx = playerPosition.x() - currentPosition.x();
        int dy = playerPosition.y() - currentPosition.y();

        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}
