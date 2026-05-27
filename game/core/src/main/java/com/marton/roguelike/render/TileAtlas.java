package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileAtlas {
    private Texture tileSheet;

    private final int TILE_SPACING = 1;
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;

    private final TextureRegion floorTile;
    private final TextureRegion wallTile;
    private final TextureRegion treeTile;



    public TileAtlas() {
        this.tileSheet = new Texture("textures/tiles.png");
        floorTile = getSpecificTile(4, 0);
        wallTile = getSpecificTile(10, 17);
        treeTile = getSpecificTile(0,1);
    }

    public Texture getTileSheet() {
        return tileSheet;
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

    public TextureRegion getFloorTile() {
        return floorTile;
    }

    public TextureRegion getWallTile() {
        return wallTile;
    }

    public TextureRegion getTreeTile() {
        return treeTile;
    }


}
