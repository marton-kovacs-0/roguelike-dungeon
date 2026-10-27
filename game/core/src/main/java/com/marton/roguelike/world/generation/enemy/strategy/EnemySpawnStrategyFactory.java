
package com.marton.roguelike.world.generation.enemy.strategy;

import com.marton.roguelike.world.generation.MapGenerator;
import com.marton.roguelike.world.generation.RoomBasedMapGenerator;

public final class EnemySpawnStrategyFactory {

    private EnemySpawnStrategyFactory() {
    }

    public static EnemySpawnStrategy createFor(MapGenerator mapGenerator) {
        if (mapGenerator instanceof RoomBasedMapGenerator) {
            return new RoomBasedEnemySpawnStrategy();
        }

        throw new IllegalArgumentException(
            "No enemy spawn strategy configured for map generator: "
                + mapGenerator.getClass().getSimpleName()
        );
    }
}
