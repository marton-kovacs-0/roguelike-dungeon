package com.marton.roguelike.entity.enemy;
import com.marton.roguelike.entity.Entity;


public abstract class Enemy extends Entity {
    public Enemy(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }
}
