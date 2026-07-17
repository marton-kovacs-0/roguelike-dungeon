package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.world.physics.CollisionHelper.canMoveTo;


public abstract class Enemy extends Entity {

    protected int attackDamage;
    protected float attackRange;
    protected static final float PLAYER_DETECTION_RANGE = 7f;
    protected static final float ATTACK_COOLDOWN = 2f;
    protected float attackCooldown = 0f;


    public Enemy(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    private void attack(Player player) {
        player.takeDamage(attackDamage);
    }

    private void tryAttack(Player player) {
        if (attackCooldown == 0f) {
            attack(player);
            attackCooldown = ATTACK_COOLDOWN;
        }
    }

    private boolean isPlayerInRange(Player player) {
        return distanceTo(player) < attackRange;
    }

    public void update(float deltaTime, Player player, GameMap gameMap) {
        updateAttackCooldown(deltaTime);

        if (!isPlayerDetected(player)) {
            return;
        }

        if (!isPlayerInRange(player)) {
            moveTowardsPlayer(player, deltaTime, gameMap);
            return;
        }

        tryAttack(player);
    }

    private void moveTowardsPlayer(Player player, float deltaTime, GameMap gameMap) {
        float moveAmount = getMoveSpeed() * deltaTime;

        moveHorizontally(player, gameMap, moveAmount);
        moveVertically(player, gameMap, moveAmount);
    }

    private void moveHorizontally(Player player, GameMap gameMap, float moveAmount) {
        if (player.getX() < this.getX()) {
            float nextX = this.getX() - moveAmount;
            if (canMoveTo(nextX, this.getY(), this, gameMap)) {
                move(-moveAmount, 0);
            }
        } else if (player.getX() > this.getX()) {
            float nextX = this.getX() + moveAmount;
            if (canMoveTo(nextX, this.getY(), this, gameMap)) {
                move(moveAmount, 0);
            }
        }
    }

    private void moveVertically(Player player, GameMap gameMap, float moveAmount) {
        if (player.getY() < this.getY()) {
            float nextY = this.getY() - moveAmount;
            if (canMoveTo(this.getX(), nextY, this, gameMap)) {
                move(0, - moveAmount);
            }
        } else if (player.getY() > this.getY()) {
            float nextY = this.getY() + moveAmount;
            if (canMoveTo(this.getX(), nextY, this, gameMap)) {
                move(0, moveAmount);
            }
        }
    }

    private void updateAttackCooldown(float deltaTime) {
        attackCooldown = Math.max(0, attackCooldown - deltaTime);
    }

    private boolean isPlayerDetected(Player player) {
        return distanceTo(player) <= PLAYER_DETECTION_RANGE;
    }

}
