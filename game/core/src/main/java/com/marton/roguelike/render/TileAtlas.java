package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileAtlas {
    private Texture tileSheet;

    private final int TILE_SPACING = 1;
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;

    private TextureRegion floorTile;



    public TileAtlas() {
        this.tileSheet = new Texture("textures/tiles.png");
        floorTile = getSpecificTile(4, 0);
    }

    public Texture getTileSheet() {
        return tileSheet;
    }

    public TextureRegion getFloorTile() {
        return floorTile;
    }

    public TextureRegion getSpecificTile(int column, int row) {
        return new TextureRegion(
            tileSheet,
            (TILE_WIDTH + TILE_SPACING) * column,
            (TILE_WIDTH + TILE_SPACING) * row,
            TILE_WIDTH,
            TILE_HEIGHT
        );
    }
}
