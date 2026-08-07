package com.marton.roguelike.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.world.GameMap;
import static com.marton.roguelike.world.physics.CollisionHelper.canMoveTo;

import java.util.List;

public class PlayerController {
    private final Player player;
    private final GameMap gameMap;

    public PlayerController(Player player, GameMap gameMap) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (gameMap == null) throw new IllegalArgumentException("Game map cannot be null");
        this.player = player;
        this.gameMap = gameMap;
    }

    public void movePlayer(float deltaTime) {
        float moveSpeed = player.getMoveSpeed();
        float moveAmount = moveSpeed * deltaTime;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            float nextY = player.getY() - moveAmount;
            if (canMoveTo(player.getX(), nextY, player, gameMap)) {
                player.move(0, - moveAmount);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            float nextY = player.getY() + moveAmount;
            if (canMoveTo(player.getX(), nextY, player, gameMap)) {
                player.move(0, moveAmount);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float nextX = player.getX() - moveAmount;
            if (canMoveTo(nextX, player.getY(), player, gameMap)) {
                player.move(-moveAmount, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float nextX = player.getX() + moveAmount;
            if (canMoveTo(nextX, player.getY(), player, gameMap)) {
                player.move(moveAmount, 0);
            }
        }
    }

    public void attack(List<Enemy> enemies) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack(enemies);
        }
    }




}
