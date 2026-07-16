package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.world.EntityType;

public class Ghost extends Enemy {
    public Ghost(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
        this.attackDamage = 10;
        this.attackRange = 2f;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.GHOST;
    }
}
