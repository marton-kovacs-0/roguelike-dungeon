package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;


public abstract class Enemy extends Entity {

    protected int attackDamage;
    protected float attackRange;
    protected static final float PLAYER_DETECTION_RANGE = 7f;

    public Enemy(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    public void attack(Player player) {
        if (isPlayerInRange(player)) {
            player.takeDamage(attackDamage);
        }
    }

    private boolean isPlayerInRange(Player player) {
        return distanceTo(player) < attackRange;
    }

    public void update(float deltaTime, Player player, GameMap gameMap) {
        moveTowardsPlayer(player, deltaTime);
    }

    private void moveTowardsPlayer(Player player, float deltaTime) {
        float moveSpeed = this.getMoveSpeed();
        float moveAmount = moveSpeed * deltaTime;

        if (distanceTo(player) <= PLAYER_DETECTION_RANGE) {
            if (player.getX() < this.getX()) {
                move(-moveAmount, 0);
            } else if (player.getX() > this.getX()) {
                move( moveAmount, 0);
            }

            if (player.getY() < this.getY()) {
                move(0, - moveAmount);
            } else if (player.getY() > this.getY()) {
                move(0, moveAmount);
            }
        }
    }
}
