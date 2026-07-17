package com.marton.roguelike.entity.enemy;

import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.world.physics.CollisionHelper.canMoveTo;


public abstract class Enemy extends Entity {

    // combat
    protected int attackDamage;
    protected float attackRange;
    protected static final float PLAYER_DETECTION_RANGE = 7f;
    protected static final float ATTACK_COOLDOWN = 2f;
    protected float attackCooldown = 0f;

    // knockback
    private float knockbackDirectionX;
    private float knockbackDirectionY;

    private static final float KNOCKBACK_SPEED = 12f;

    protected static final float KNOCKBACK_DURATION = 0.1f;
    private float knockbackTimeRemaining = 0;


    public Enemy(float x, float y, int maxHealth) {
        super(x, y, maxHealth);
    }

    /**
     * Updates the enemy at each frame.
     * Handles combat state, knockback, movement and attacking.
     */
    public void update(float deltaTime, Player player, GameMap gameMap) {
        updateAttackCooldown(deltaTime);

        if (updateKnockback(deltaTime, gameMap)) {
            return;
        }

        if (!isPlayerDetected(player)) {
            return;
        }

        if (!isPlayerInRange(player)) {
            moveTowardsPlayer(player, deltaTime, gameMap);
            return;
        }

        tryAttack(player);
    }

    /**
     * Applies knockback away from the player.
     * Calculates and stores the knockback direction.
     */
    public void knockback(Player player) {
        float dx = getX() - player.getX();
        float dy = getY() - player.getY();
        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if (length == 0) {
            return;
        }

        knockbackDirectionX = dx / length;
        knockbackDirectionY = dy / length;
        knockbackTimeRemaining = KNOCKBACK_DURATION;
    }

    /**
     * Moves the enemy while knockback is active.
     * Returns true while knockback is still in progress.
     */
    private boolean updateKnockback(float deltaTime, GameMap gameMap) {
        if (knockbackTimeRemaining > 0) {
            float moveX = knockbackDirectionX * KNOCKBACK_SPEED * deltaTime;
            float moveY = knockbackDirectionY * KNOCKBACK_SPEED * deltaTime;

            float nextX = getX() + moveX;
            float nextY = getY() + moveY;

            if (canMoveTo(nextX, nextY, this, gameMap)) {
                move(moveX, moveY);
            }

            if (knockbackTimeRemaining > 0) {
                knockbackTimeRemaining -= deltaTime;
                return knockbackTimeRemaining > 0;
            }
        }
        return false;
    }

    // Moves the enemy towards the player using collision checks.
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


    // Attempts to attack the player if the attack cooldown has expired.
    private void tryAttack(Player player) {
        if (attackCooldown == 0f) {
            attack(player);
            attackCooldown = ATTACK_COOLDOWN;
        }
    }

    private void attack(Player player) {
        player.takeDamage(attackDamage);
    }

    private boolean isPlayerInRange(Player player) {
        return distanceTo(player) < attackRange;
    }

    private boolean isPlayerDetected(Player player) {
        return distanceTo(player) <= PLAYER_DETECTION_RANGE;
    }
    private void updateAttackCooldown(float deltaTime) {
        attackCooldown = Math.max(0, attackCooldown - deltaTime);
    }

}
