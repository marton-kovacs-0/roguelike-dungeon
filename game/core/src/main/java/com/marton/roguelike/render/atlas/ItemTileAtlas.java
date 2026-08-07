package com.marton.roguelike.render.atlas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.item.ItemType;

public class ItemTileAtlas {
    private final Texture tileSheet;
    private static final int TILE_SPACING = 1;
    private static final int TILE_SIZE = 16;

    private final TextureRegion sword;
    private final TextureRegion gold;
    private final TextureRegion healthPotion;

    public ItemTileAtlas() {
        this.tileSheet = new Texture("textures/tiles.png");
        this.sword = getSpecificTile(0, 29);
        this.gold = getSpecificTile(22 ,4);
        this.healthPotion = getSpecificTile(17, 25);
    }

    public TextureRegion getRegion(ItemType itemType) {
        return switch (itemType) {
            case SWORD -> sword;
            case GOLD -> gold;
            case HEALTH_POTION -> healthPotion;
        };
    }

    public TextureRegion getSpecificTile(int column, int row) {
        return new TextureRegion(
            tileSheet,
            (TILE_SIZE + TILE_SPACING) * column,
            (TILE_SIZE + TILE_SPACING) * row,
            TILE_SIZE,
            TILE_SIZE
        );
    }
}
