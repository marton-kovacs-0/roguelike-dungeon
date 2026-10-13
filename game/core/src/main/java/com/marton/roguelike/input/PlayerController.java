package com.marton.roguelike.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.item.Item;
import com.marton.roguelike.item.ItemType;
import com.marton.roguelike.item.SimpleItem;
import com.marton.roguelike.item.WorldItem;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import static com.marton.roguelike.world.physics.CollisionHelper.canMoveTo;

import java.util.List;

public class PlayerController {
    private final Player player;
    private static final float PICKUP_RANGE = 1.5f;
    private boolean inventoryOpen = false;

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

    public void pickUpItem() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            float playerCenterX = player.getX() + player.getWidth() / 2f;
            float playerCenterY = player.getY() + player.getHeight() / 2f;

            WorldItem worldItem = gameMap.getClosestWorldItem(
                playerCenterX,
                playerCenterY,
                PICKUP_RANGE
            );

            if (worldItem == null) {
                return;
            }

            boolean itemAdded = player.addItemToInventory(worldItem.getItem());

            if (itemAdded) {
                gameMap.removeWorldItem(worldItem);
            }
        }
    }

    public void deleteItemFromInventory(int index) {
        List<Item> inventory = player.getInventory();

        if (index >= inventory.size() || index < 0) {
            return;
        }

        Item itemToDelete = inventory.get(index);
        if (itemToDelete.getItemType() != ItemType.KEY) {
            player.removeItemFromInventory(itemToDelete);
        }
    }

    public void dropItemFromInventory(int index) {
        List<Item> inventory = player.getInventory();

        if (index >= inventory.size() || index < 0) {
            return;
        }

        Item itemToDrop = inventory.get(index);
        TilePosition currentPlayerPosition = new TilePosition(
            (int) Math.floor(player.getX()),
            (int) Math.floor(player.getY()));

        if (player.removeItemFromInventory(itemToDrop)) {
            WorldItem droppedItem = new WorldItem(itemToDrop, currentPlayerPosition);
            gameMap.addWorldItem(droppedItem);
        }

    }

    public void toggleInventory() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            inventoryOpen = !inventoryOpen;
        }
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }

}
