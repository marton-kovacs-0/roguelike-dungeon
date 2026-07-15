package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.world.EntityType;

public class Skeleton extends Enemy {
    public Skeleton(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }
}
