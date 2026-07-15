package com.marton.roguelike.render.atlas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.world.EntityType;

public class EntityTileAtlas {
    private Texture tileSheet;

    private static final int TILE_SPACING = 1;
    private static final int TILE_SIZE = 16;

    private TextureRegion playerTile;

    public EntityTileAtlas() {
        this.tileSheet = new Texture("textures/tiles.png");
        playerTile = getSpecificTile(25, 0);
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

    public TextureRegion getRegion(EntityType entityType) {
        switch (entityType) {
            case PLAYER: return playerTile;
        }
        throw new IllegalArgumentException("Invalid CellType.");
    }

    public void dispose() {
        tileSheet.dispose();
    }
}
