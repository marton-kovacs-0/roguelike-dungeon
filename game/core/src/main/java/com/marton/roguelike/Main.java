package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.render.TileAtlas;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private TextureRegion floorTile;
    private TileAtlas atlas;

    @Override
    public void create() {
        atlas = new TileAtlas();
        floorTile = atlas.getFloorTile();
        batch = new SpriteBatch();

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(floorTile, 140, 210, 64, 64);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
