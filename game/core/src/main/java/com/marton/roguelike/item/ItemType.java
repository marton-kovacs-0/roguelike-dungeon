package com.marton.roguelike.item;

public enum ItemType {
    SWORD(ItemCategory.WEAPON, "Iron Sword", "Just a regular iron sword", false, 1),
    GOLD(ItemCategory.MATERIAL, "Gold", "Currency", true, 9999),
    HEALTH_POTION(ItemCategory.CONSUMABLE,"Health Potion", "restores health", true, 10);

    private final ItemCategory itemCategory;
    private final String displayName;
    private final String description;
    private final boolean isStackable;
    private final int maxStack;


    ItemType(ItemCategory itemCategory, String displayName, String description, boolean isStackable, int maxStack) {
        this.itemCategory = itemCategory;
        this.displayName = displayName;
        this.description = description;
        this.isStackable = isStackable;
        this.maxStack = maxStack;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public int getMaxStack() {
        return maxStack;
    }
}
