package com.marton.roguelike.world.generation.enemy;

import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.entity.enemy.Ghost;
import com.marton.roguelike.entity.enemy.Ogre;
import com.marton.roguelike.entity.enemy.Skeleton;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner {

    // Chance (0-100%) that a room will contain an enemy.
    private static final int SPAWN_CHANCE = 50;

    private static final int ENEMY_TYPE_COUNT = 3;
    private final Random random = new Random();

    // Spawns random enemies in random rooms.
    public List<Enemy> spawnEnemies(GameMap gameMap, List<Room> rooms) {
        List<Enemy> enemies = new ArrayList<>();

        for (Room room : rooms) {
            if (random.nextInt(100) < SPAWN_CHANCE) {
                List<TilePosition> walkablePositions = getWalkablePositions(gameMap, room);
                TilePosition spawnPosition = walkablePositions.get(random.nextInt(walkablePositions.size()));
                enemies.add(createRandomEnemy(spawnPosition));
            }
        }

        return enemies;
    }

    // Returns all walkable positions inside the room.
    private List<TilePosition> getWalkablePositions(GameMap gameMap, Room room) {
        return room.getFloorPositions().stream()
            .filter(gameMap::isWalkable).toList();
    }

    // Creates a random enemy type at the given position.
    private Enemy createRandomEnemy(TilePosition position) {
        int enemyType = random.nextInt(ENEMY_TYPE_COUNT);

        switch (enemyType) {
            case 0: return new Skeleton(position.x(), position.y(), 100);
            case 1: return new Ghost(position.x(), position.y(), 50);
            case 2: return new Ogre(position.x(), position.y(), 200);
            default: throw new IllegalStateException("Unexpected enemy type");
        }
    }
}
