
package com.marton.roguelike.world.generation.enemy.strategy;

import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomEnemySpawnStrategy implements EnemySpawnStrategy {

    private final Random random = new Random();

    @Override
    public List<TilePosition> selectSpawnPositions(
        MapGenerationResult generationResult,
        TilePosition playerPosition,
        int enemyCountTarget,
        int minimumDistance) {

        GameMap gameMap = generationResult.getGameMap();
        List<TilePosition> candidates = new ArrayList<>();

        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                if (!gameMap.isWalkable(x, y)) {
                    continue;
                }

                TilePosition position = new TilePosition(x, y);

                if (position.equals(playerPosition)) {
                    continue;
                }

                int dx = x - playerPosition.x();
                int dy = y - playerPosition.y();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance >= minimumDistance) {
                    candidates.add(position);
                }
            }
        }

        Collections.shuffle(candidates, random);

        int spawnCount = Math.min(enemyCountTarget, candidates.size());

        return new ArrayList<>(candidates.subList(0, spawnCount));
    }
}
