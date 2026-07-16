package com.marton.roguelike.world.physics;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.world.GameMap;

public final class CollisionHelper {


    public static boolean canMoveTo(float nextX, float nextY, Entity entity, GameMap gameMap) {
        float left = nextX;
        float right = nextX + entity.getWidth();
        float bottom = nextY;
        float top = nextY + entity.getHeight();

        int leftTile = (int) Math.floor(left);
        int rightTile = (int) Math.floor(right - 0.001f);
        int bottomTile = (int) Math.floor(bottom);
        int topTile = (int) Math.floor(top - 0.001f);

        return gameMap.isWalkable(leftTile, bottomTile)
            && gameMap.isWalkable(rightTile, bottomTile)
            && gameMap.isWalkable(leftTile, topTile)
            && gameMap.isWalkable(rightTile, topTile);
    }
}
