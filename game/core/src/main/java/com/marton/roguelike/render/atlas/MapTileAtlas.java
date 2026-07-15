package com.marton.roguelike.render.atlas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.world.MapCellType;

public class MapTileAtlas {
    private Texture tileSheet;

    private static final int TILE_SPACING = 1;
    private static final int TILE_SIZE = 16;

    private final TextureRegion emptyTile;
    private final TextureRegion floorTile;
    private final TextureRegion wallTile;
    private final TextureRegion treeTile;
    private final TextureRegion closedChestTile;



    public MapTileAtlas() {
        this.tileSheet = new Texture("textures/tiles.png");
        emptyTile = getSpecificTile(0, 0);
        floorTile = getSpecificTile(4, 0);
        wallTile = getSpecificTile(10, 17);
        treeTile = getSpecificTile(0,1);
        closedChestTile = getSpecificTile(8, 6);
    }

    public TextureRegion getRegion(MapCellType mapCellType) {
        switch (mapCellType) {
            case WALL: return wallTile;
            case FLOOR: return floorTile;
            case TREE: return treeTile;
            case EMPTY: return emptyTile;
            case CLOSED_CHEST: return closedChestTile;
        }
        throw new IllegalArgumentException("Invalid CellType.");
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

    public void dispose() {
        tileSheet.dispose();
    }
}
