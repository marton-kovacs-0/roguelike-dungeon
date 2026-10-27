package com.marton.roguelike.world.generation.metadata;

import com.marton.roguelike.world.GameMap;

public class BasicMapGenerationResult implements MapGenerationResult {

    private final GameMap gameMap;

    public BasicMapGenerationResult(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }
}
