package com.marton.roguelike.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;

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

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            float nextY = player.getY() - moveAmount;
            if (canMoveTo(player.getX(), nextY)) {
                player.move(0, -moveAmount);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            float nextY = player.getY() + moveAmount;
            if (canMoveTo(player.getX(), nextY)) {
                player.move(0, moveAmount);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            float nextX = player.getX() - moveAmount;
            if (canMoveTo(nextX, player.getY())) {
                player.move(-moveAmount, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            float nextX = player.getX() + moveAmount;
            if (canMoveTo(nextX, player.getY())) {
                player.move(moveAmount, 0);
            }
        }
    }

    private boolean canMoveTo(float nextX, float nextY) {
        float left = nextX;
        float right = nextX + player.getWidth();
        float bottom = nextY;
        float top = nextY + player.getHeight();

        int leftTile = (int) left;
        int rightTile = (int) (right - 0.001f);
        int bottomTile = (int) bottom;
        int topTile = (int) (top - 0.001f);

        return gameMap.isWalkable(leftTile, bottomTile)
            && gameMap.isWalkable(rightTile, bottomTile)
            && gameMap.isWalkable(leftTile, topTile)
            && gameMap.isWalkable(rightTile, topTile);
    }


}
