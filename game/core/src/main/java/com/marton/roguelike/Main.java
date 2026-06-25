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
import com.marton.roguelike.world.*;
import com.marton.roguelike.world.generation.MapGenerator;
import com.marton.roguelike.world.generation.RoomBasedMapGenerator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private MapRenderer renderer;
    private MapLoader mapLoader;
    private MapGenerator mapGenerator;
    private GameMap gameMap;
    private String testMapPath = "assets/maps/testmap.txt";

    private OrthographicCamera camera;
    private CameraController cameraController;

    private Player player;
    private PlayerController playerController;


    @Override
    public void create() {
        TileAtlas atlas = new TileAtlas();

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
        mapGenerator = new RoomBasedMapGenerator();
        try {
            gameMap = mapGenerator.generate(1000, 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Cell spawnPoint = gameMap.getRandomWalkableCell();
        player = new Player(spawnPoint.getX(), spawnPoint.getY());
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
