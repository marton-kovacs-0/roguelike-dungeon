package com.marton.roguelike.item;

import com.marton.roguelike.world.TilePosition;

public class WorldItem {
    private final Item item;
    private final TilePosition position;

    public WorldItem(Item item, TilePosition position) {
        this.item = item;
        this.position = position;
    }

    public Item getItem() {
        return item;
    }

    public TilePosition getPosition() {
        return position;
    }
}
