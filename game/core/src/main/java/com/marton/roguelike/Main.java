package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.input.PlayerController;
import com.marton.roguelike.render.MapRenderer;
import com.marton.roguelike.render.TileAtlas;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapLoader;

import java.io.IOException;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private MapRenderer renderer;
    private MapLoader mapLoader;
    private GameMap gameMap;
    private String testMapPath = "assets/maps/testmap.txt";
    private Player player;
    private PlayerController playerController;


    @Override
    public void create() {
        TileAtlas atlas = new TileAtlas();
        renderer = new MapRenderer(atlas);
        mapLoader = new MapLoader();
        try {
            gameMap = mapLoader.loadMap(testMapPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player = new Player(3, 3);
        this.playerController = new PlayerController(player, gameMap);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        playerController.movePlayer();
        renderer.render(gameMap, player);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }


}
