package com.marton.roguelike.entity;

import com.marton.roguelike.world.EntityType;

public abstract class Entity {
    private float x;
    private float y;
    private static final float MOVE_SPEED = 6f;
    private static final float WIDTH = 0.8f;
    private static final float HEIGHT = 0.8f;

    private final int MAX_HEALTH;
    private int currentHealth;

    public Entity(float x, float y, int maxHealth) {
        if (x < 0) throw new IllegalArgumentException("Invalid x coordinate");
        if (y < 0) throw new IllegalArgumentException("Invalid y coordinate");

        this.x = x;
        this.y = y;
        this.MAX_HEALTH = maxHealth;
        this.currentHealth = maxHealth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public float getMoveSpeed() {
        return MOVE_SPEED;
    }

    public float getWidth() {
        return WIDTH;
    }

    public float getHeight() {
        return HEIGHT;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public abstract EntityType getEntityType();
}
