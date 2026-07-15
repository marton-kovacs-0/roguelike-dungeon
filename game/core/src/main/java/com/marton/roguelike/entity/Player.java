package com.marton.roguelike.entity;

import com.marton.roguelike.world.EntityType;

public class Player extends Entity{
    public Player(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }
}
