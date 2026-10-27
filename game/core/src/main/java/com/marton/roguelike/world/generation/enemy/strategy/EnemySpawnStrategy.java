package com.marton.roguelike.world.generation.enemy.strategy;

import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

import java.util.List;

public interface EnemySpawnStrategy {

    List<TilePosition> selectSpawnPositions(
        MapGenerationResult generationResult,
        TilePosition playerPosition,
        int enemyCountTarget,
        int minimumDistance);
}
