package com.marton.roguelike.world.generation;

import com.marton.roguelike.world.GameMap;

public interface MapGenerator {
    GameMap generate(int width, int height);
}
