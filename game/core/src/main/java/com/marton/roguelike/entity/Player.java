package com.marton.roguelike.entity;

import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.world.EntityType;

import java.util.List;

public class Player extends Entity{
    private static final float ATTACK_RANGE = 1.5f;
    private int attackDamage = 30;

    public Player(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    // Finds an enemy in range and damages it.
    // The enemy also attacks the player if not dead.
    public void attack(List<Enemy> enemies) {
        Enemy enemyToAttack = findEnemyInRange(enemies);

        if (enemyToAttack != null) {
            enemyToAttack.takeDamage(attackDamage);

            if (enemyToAttack.isDead()) {
                enemies.remove(enemyToAttack);
                return;
            }

            enemyToAttack.attack(this);
        }
    }

    private Enemy findEnemyInRange(List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }

        for (Enemy enemy : enemies) {
            if (distanceTo(enemy) < ATTACK_RANGE) {
                return enemy;
            }
        }
        return null;
    }
}
