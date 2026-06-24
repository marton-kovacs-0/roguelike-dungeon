package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.input.PlayerController;
import com.marton.roguelike.render.CameraController;
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

    private OrthographicCamera camera;
    private CameraController cameraController;

    private Player player;
    private PlayerController playerController;


    @Override
    public void create() {
        TileAtlas atlas = new TileAtlas();
        player = new Player(3, 3);

        this.camera = new OrthographicCamera();
        camera.setToOrtho(
            false,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        cameraController = new CameraController(camera);
        Gdx.input.setInputProcessor(cameraController);

        renderer = new MapRenderer(atlas, camera);
        mapLoader = new MapLoader();
        try {
            gameMap = mapLoader.loadMap(testMapPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.playerController = new PlayerController(player, gameMap);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        playerController.movePlayer(deltaTime);
        cameraController.update(player.getX(), player.getY(), gameMap.getHeight());
        renderer.render(gameMap, player);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }


}
