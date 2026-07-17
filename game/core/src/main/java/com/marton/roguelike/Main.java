package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.input.PlayerController;
import com.marton.roguelike.render.CameraController;
import com.marton.roguelike.render.EntityRenderer;
import com.marton.roguelike.render.MapRenderer;
import com.marton.roguelike.render.UiRenderer;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.render.atlas.MapTileAtlas;
import com.marton.roguelike.world.*;
import com.marton.roguelike.world.generation.MapGenerator;
import com.marton.roguelike.world.generation.RoomBasedMapGenerator;
import com.marton.roguelike.world.generation.enemy.EnemySpawner;

import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private MapRenderer mapRenderer;
    private EntityRenderer entityRenderer;
    private UiRenderer uiRenderer;
    private EnemySpawner enemySpawner;
    private RoomBasedMapGenerator mapGenerator;
    private GameMap gameMap;
    private MapTileAtlas mapTileAtlas;
    private EntityTileAtlas entityTileAtlas;

    private OrthographicCamera camera;
    private CameraController cameraController;

    private Player player;
    private List<Enemy> enemies;
    private PlayerController playerController;

    private boolean gameOver = false;


    @Override
    public void create() {
        mapTileAtlas = new MapTileAtlas();
        entityTileAtlas = new EntityTileAtlas();
        enemySpawner = new EnemySpawner();

        this.camera = new OrthographicCamera();
        camera.setToOrtho(
            false,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        cameraController = new CameraController(camera);
        Gdx.input.setInputProcessor(cameraController);

        mapRenderer = new MapRenderer(mapTileAtlas);
        mapGenerator = new RoomBasedMapGenerator();
        try {
            gameMap = mapGenerator.generate(100, 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityRenderer = new EntityRenderer(entityTileAtlas);

        // Spawn player and enemies.
        Cell spawnPoint = gameMap.getRandomWalkableCell();
        player = new Player(spawnPoint.getPosition().x(), spawnPoint.getPosition().y(), 1000);
        playerController = new PlayerController(player, gameMap);
        uiRenderer = new UiRenderer();
        enemies = enemySpawner.spawnEnemies(gameMap, mapGenerator.getRooms());

    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (player.isDead() && !gameOver) {
            gameOver = true;
            System.out.println("GAME OVER");
        }

        if (!gameOver) {
            playerController.movePlayer(deltaTime);
            playerController.attack(enemies);
            cameraController.update(player.getX(), player.getY(), gameMap.getHeight());
        }

        for (Enemy enemy : enemies) {
            enemy.update(deltaTime, player, gameMap);
        }
        player.update(deltaTime);

        mapRenderer.render(gameMap, camera);
        entityRenderer.render(gameMap, player, enemies, camera);
        uiRenderer.render(player);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        entityRenderer.dispose();
        mapTileAtlas.dispose();
        entityTileAtlas.dispose();
        uiRenderer.dispose();
    }
}
