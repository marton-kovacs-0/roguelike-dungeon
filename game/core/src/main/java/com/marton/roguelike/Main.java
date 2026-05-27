package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.render.TileAtlas;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private TileAtlas atlas;
    private static final int RENDER_TILE_SIZE = 64;
    private static final int MAP_OFFSET_X = 100;
    private static final int MAP_OFFSET_Y = 100;

    @Override
    public void create() {
        atlas = new TileAtlas();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        drawTile(atlas.getWallTile(), 1, 1);
        drawTile(atlas.getWallTile(), 2, 1);
        drawTile(atlas.getWallTile(), 3, 1);
        drawTile(atlas.getWallTile(), 4, 1);

        drawTile(atlas.getWallTile(), 1, 2);
        drawTile(atlas.getFloorTile(), 2, 2);
        drawTile(atlas.getFloorTile(), 3, 2);
        drawTile(atlas.getFloorTile(), 4, 2);

        drawTile(atlas.getWallTile(), 1, 3);
        drawTile(atlas.getWallTile(), 2, 3);
        drawTile(atlas.getWallTile(), 3, 3);
        drawTile(atlas.getWallTile(), 4, 3);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
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
}
