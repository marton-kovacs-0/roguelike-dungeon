package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;

public class MapRenderer {

    private SpriteBatch batch;
    private TileAtlas atlas;
    private static final int RENDER_SCALE = 3;
    private static final int SOURCE_TILE_SIZE = 16;
    private static final int RENDER_TILE_SIZE = RENDER_SCALE * SOURCE_TILE_SIZE;
    private static final int MAP_OFFSET_X = 100;
    private static final int MAP_OFFSET_Y = 100;

    public MapRenderer(TileAtlas atlas) {
        this.batch = new SpriteBatch();
        this.atlas = atlas;
    }

    public void render(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();
        batch.begin();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameMap.getCell(x, y);
                int renderY = height - 1 - y;
                drawTile(atlas.getRegionForType(cell.getCellType()), x, renderY);
            }
        }
        batch.end();
    }

    private void drawTile(TextureRegion region, int tileX, int tileY) {
        batch.draw(
            region,
            MAP_OFFSET_X + tileX * RENDER_TILE_SIZE,
            MAP_OFFSET_Y + tileY * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );
    }

    public void dispose() {
        batch.dispose();
    }
}
