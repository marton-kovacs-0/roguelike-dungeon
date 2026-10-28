
package com.marton.roguelike.world.generation.enemy.strategy;

import com.marton.roguelike.world.generation.map.CellularAutomataMapGenerator;
import com.marton.roguelike.world.generation.map.MapGenerator;
import com.marton.roguelike.world.generation.map.RoomBasedMapGenerator;

public final class EnemySpawnStrategyFactory {

    private EnemySpawnStrategyFactory() {
    }

    public static EnemySpawnStrategy createFor(MapGenerator mapGenerator) {
        if (mapGenerator instanceof RoomBasedMapGenerator) {
            return new RoomBasedEnemySpawnStrategy();
        }

        if (mapGenerator instanceof CellularAutomataMapGenerator) {
            return new RandomEnemySpawnStrategy();
        }

        throw new IllegalArgumentException(
            "No enemy spawn strategy configured for map generator: "
                + mapGenerator.getClass().getSimpleName()
        );
    }
}
