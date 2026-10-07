package com.marton.roguelike.world.generation.item;

import com.marton.roguelike.item.Item;
import com.marton.roguelike.item.ItemType;
import com.marton.roguelike.item.SimpleItem;
import com.marton.roguelike.item.WorldItem;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class ItemSpawner {
    private static final int MAX_SWORD_THRESHOLD = 3;
    private static final int MAX_HEALTH_POTION_THRESHOLD = 5;
    private static final int NEXT_LEVEL_KEY = 1;


    private static final int MIN_GOLD_AMOUNT = 5;
    private static final int MAX_GOLD_THRESHOLD = 20;

    private final Random random = new Random();


    public void spawnItems(GameMap gameMap) {
        Set<TilePosition> occupiedPositions = new HashSet<>();

        int swords = random.nextInt(1, MAX_SWORD_THRESHOLD);
        int healthPotions = random.nextInt(1, MAX_HEALTH_POTION_THRESHOLD + 1);
        int gold = random.nextInt(MIN_GOLD_AMOUNT, MAX_GOLD_THRESHOLD);

        spawnSpecificItem(gameMap, swords, ItemType.SWORD, occupiedPositions);
        spawnSpecificItem(gameMap, healthPotions, ItemType.HEALTH_POTION, occupiedPositions);
        spawnSpecificItem(gameMap, gold, ItemType.GOLD, occupiedPositions);
        spawnSpecificItem(gameMap, 1, ItemType.KEY, occupiedPositions);

    }

    private void spawnSpecificItem(
        GameMap gameMap,
        int amount,
        ItemType itemType,
        Set<TilePosition> occupiedPositions) {

        int counter = 0;

        while (counter < amount) {
            TilePosition position = gameMap.getRandomWalkableCell().getPosition();

            if (!occupiedPositions.contains(position)) {
                occupiedPositions.add(position);
                WorldItem item = new WorldItem(new SimpleItem(occupiedPositions.size(), itemType), position);
                gameMap.addWorldItem(item);
                counter++;
            }
        }
    }


}
