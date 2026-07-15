package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.world.EntityType;

public class Ogre extends Enemy {
    public Ogre(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.OGRE;
    }
}
