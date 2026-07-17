package com.marton.roguelike.entity.enemy;
import com.marton.roguelike.world.EntityType;

public class Skeleton extends Enemy {
    public Skeleton(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
        this.attackDamage = 15;
        this.attackRange = 1.5f;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }
}
