package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.generation.metadata.MapGenerationResult;

public interface MapGenerator {
    MapGenerationResult generate(int width, int height);
}
