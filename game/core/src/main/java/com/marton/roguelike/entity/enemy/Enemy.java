package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;
import static com.marton.roguelike.world.physics.CollisionHelper.canMoveTo;


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
        moveTowardsPlayer(player, deltaTime, gameMap);
    }

    private void moveTowardsPlayer(Player player, float deltaTime, GameMap gameMap) {
        float moveSpeed = this.getMoveSpeed();
        float moveAmount = moveSpeed * deltaTime;

        if (distanceTo(player) <= PLAYER_DETECTION_RANGE) {

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
    }

}
