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

    public void movePlayer() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (canMoveTo(player.getX(), player.getY() - 1)) {
                player.move(0, -1);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (canMoveTo(player.getX(), player.getY() + 1)) {
                player.move(0, 1);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (canMoveTo(player.getX() - 1, player.getY())) {
                player.move(-1, 0);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (canMoveTo(player.getX() + 1, player.getY())) {
                player.move(1, 0);
            }
        }
    }

    private boolean canMoveTo(int newX, int newY) {
        return gameMap.isInsideMap(newX, newY)
            && gameMap.isWalkable(newX, newY);
    }


}
