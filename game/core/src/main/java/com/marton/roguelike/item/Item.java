package com.marton.roguelike.item;

public abstract class Item {
    private final int id;
    private final ItemType itemType;


    public Item(int id, ItemType itemType) {
        this.id = id;
        this.itemType = itemType;
    }

    public int getId() {
        return id;
    }

    public ItemCategory getItemCategory() {
        return itemType.getItemCategory();
    }

    public String getName() {
        return itemType.getDisplayName();
    }

    public String getDescription() {
        return itemType.getDescription();
    }

    public boolean isStackable() {
        return itemType.isStackable();
    }

    public int getMaxStack() {
        return itemType.getMaxStack();
    }

    public ItemType getItemType() {
        return itemType;
    }
}
