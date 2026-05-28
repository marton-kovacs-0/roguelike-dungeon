package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.render.MapRenderer;
import com.marton.roguelike.render.TileAtlas;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapLoader;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private MapRenderer renderer;
    private MapLoader mapLoader;
    private GameMap gameMap;


    @Override
    public void create() {
        TileAtlas atlas = new TileAtlas();
        renderer = new MapRenderer(atlas);
        mapLoader = new MapLoader();
        gameMap = mapLoader.loadMap();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        renderer.render(gameMap);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }


}
