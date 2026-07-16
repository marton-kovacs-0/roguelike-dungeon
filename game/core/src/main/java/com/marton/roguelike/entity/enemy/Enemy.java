package com.marton.roguelike.entity.enemy;
import com.marton.roguelike.entity.Entity;
import com.marton.roguelike.entity.Player;


public abstract class Enemy extends Entity {

    protected int attackDamage;
    protected float attackRange;

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
}
