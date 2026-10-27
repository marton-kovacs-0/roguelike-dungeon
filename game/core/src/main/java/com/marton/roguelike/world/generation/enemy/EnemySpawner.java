
package com.marton.roguelike.world.generation.enemy;

import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.entity.enemy.Ghost;
import com.marton.roguelike.entity.enemy.Ogre;
import com.marton.roguelike.entity.enemy.Skeleton;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.enemy.strategy.EnemySpawnStrategy;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner {

    private static final double ENEMY_DENSITY = 0.01;
    private static final int MINIMUM_DISTANCE = 5;
    private static final int ENEMY_TYPE_COUNT = 3;

    private final Random random = new Random();
    private final EnemySpawnStrategy spawnStrategy;

    public EnemySpawner(EnemySpawnStrategy spawnStrategy) {
        this.spawnStrategy = spawnStrategy;
    }

    public List<Enemy> spawnEnemies(
        MapGenerationResult generationResult,
        TilePosition playerPosition) {

        GameMap gameMap = generationResult.getGameMap();

        int walkableTileCount = 0;

        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                if (gameMap.isWalkable(x, y)) {
                    walkableTileCount++;
                }
            }
        }

        int enemyCountTarget =
            (int) (walkableTileCount * ENEMY_DENSITY);

        List<TilePosition> spawnPositions =
            spawnStrategy.selectSpawnPositions(
                generationResult,
                playerPosition,
                enemyCountTarget,
                MINIMUM_DISTANCE
            );

        List<Enemy> enemies = new ArrayList<>();

        for (TilePosition position : spawnPositions) {
            enemies.add(createRandomEnemy(position));
        }

        return enemies;
    }

    private Enemy createRandomEnemy(TilePosition position) {
        int enemyType = random.nextInt(ENEMY_TYPE_COUNT);

        switch (enemyType) {
            case 0:
                return new Skeleton(position.x(), position.y(), 100);
            case 1:
                return new Ghost(position.x(), position.y(), 50);
            case 2:
                return new Ogre(position.x(), position.y(), 200);
            default:
                throw new IllegalStateException("Unexpected enemy type");
        }
    }
}
