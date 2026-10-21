package com.marton.roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.input.PlayerController;

import com.marton.roguelike.render.*;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.render.atlas.ItemTileAtlas;
import com.marton.roguelike.render.atlas.MapTileAtlas;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;

import com.marton.roguelike.world.generation.RoomBasedMapGenerator;
import com.marton.roguelike.world.generation.enemy.EnemySpawner;
import com.marton.roguelike.world.generation.item.ItemSpawner;
import com.marton.roguelike.world.visibility.FieldOfView;
import com.marton.roguelike.world.visibility.VisibilityMap;
import com.marton.roguelike.world.visibility.VisibilityMask;

import java.util.List;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    // Renderers
    private MapRenderer mapRenderer;
    private EntityRenderer entityRenderer;
    private UiRenderer uiRenderer;
    private ItemRenderer itemRenderer;
    private ScreenTransition screenTransition;

    // Generators and spawners
    private RoomBasedMapGenerator mapGenerator;
    private EnemySpawner enemySpawner;
    private ItemSpawner itemSpawner;

    // Map and entities
    private GameMap gameMap;
    private Player player;
    private List<Enemy> enemies;

    // Controllers
    private PlayerController playerController;
    private CameraController cameraController;

    // Tile atlases
    private MapTileAtlas mapTileAtlas;
    private EntityTileAtlas entityTileAtlas;
    private ItemTileAtlas itemTileAtlas;

    // Camera
    private OrthographicCamera camera;

    // Game state
    private boolean gameOver = false;

    // FOV
    private VisibilityMap visibilityMap;
    private FieldOfView fieldOfView;
    private VisibilityMask visibilityMask;
    private boolean debugMapVisible = false;

    @Override
    public void create() {

        // Initialize atlases
        mapTileAtlas = new MapTileAtlas();
        entityTileAtlas = new EntityTileAtlas();
        itemTileAtlas = new ItemTileAtlas();

        // Initialize generators and spawners
        mapGenerator = new RoomBasedMapGenerator();
        enemySpawner = new EnemySpawner();
        itemSpawner = new ItemSpawner();

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(
            false,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        cameraController = new CameraController(
            camera,
            () -> debugMapVisible = !debugMapVisible);
        Gdx.input.setInputProcessor(cameraController);

        screenTransition = new ScreenTransition();

        // Generate the initial dungeon
        try {
            gameMap = mapGenerator.generate(100, 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Spawn player
        Cell spawnPoint = gameMap.getRandomWalkableCell();
        player = new Player(
            spawnPoint.getPosition().x(),
            spawnPoint.getPosition().y(),
            1000
        );

        // Initialize player controller
        playerController = new PlayerController(player, gameMap);

        // Initialize visibility, field of view and mask
        visibilityMap = new VisibilityMap(
            gameMap.getWidth(),
            gameMap.getHeight()
        );

        fieldOfView = new FieldOfView(gameMap, visibilityMap, player);
        fieldOfView.update();

        visibilityMask = new VisibilityMask(gameMap);
        visibilityMask.update(player);

        // Initialize renderers
        mapRenderer = new MapRenderer(mapTileAtlas, visibilityMap);
        mapRenderer.setVisibilityMask(visibilityMask);
        entityRenderer = new EntityRenderer(entityTileAtlas);
        itemRenderer = new ItemRenderer(itemTileAtlas);
        uiRenderer = new UiRenderer();

        // Spawn enemies and items
        enemies = enemySpawner.spawnEnemies(gameMap, mapGenerator.getRooms());
        itemSpawner.spawnItems(gameMap);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Update the screen transition.
        screenTransition.update(deltaTime);

        // Change the dungeon only after the screen is fully black.
        if (screenTransition.isWaitingForLevelChange()) {
            changeLevel();
            screenTransition.startFadeIn();
        }

        if (player.isDead() && !gameOver) {
            gameOver = true;
            System.out.println("GAME OVER");
        }

        // Freeze gameplay while the transition is active.
        if (!gameOver && !screenTransition.isActive()) {
            playerController.movePlayer(deltaTime);
            playerController.attack(enemies);

            if (playerController.handleInteraction(uiRenderer)) {
                screenTransition.start();
            }

            playerController.toggleInventory();

            cameraController.update(
                player.getX(),
                player.getY(),
                gameMap.getHeight()
            );
        }

        // Enemies and player animations should also pause during transitions.
        if (!screenTransition.isActive()) {
            for (Enemy enemy : enemies) {
                enemy.update(deltaTime, player, gameMap);
            }

            player.update(deltaTime);
        }
        // updates field of view when the player moves
        fieldOfView.update();
        visibilityMask.update(player);

        mapRenderer.render(gameMap, camera, debugMapVisible);
        entityRenderer.render(gameMap, player, enemies, camera);

        // Handle inventory UI and drop/delete.
        boolean inventoryOpen = playerController.isInventoryOpen();
        uiRenderer.render(player, inventoryOpen, deltaTime);

        if (inventoryOpen && !screenTransition.isActive()) {
            uiRenderer.handleButtonClick(
                player.getInventory(),
                playerController
            );
        }

        itemRenderer.render(gameMap, camera);

        // Draw the transition overlay last so it covers the entire scene.
        screenTransition.render();
    }

    @Override
    public void dispose() {

        mapRenderer.dispose();
        entityRenderer.dispose();
        mapTileAtlas.dispose();
        entityTileAtlas.dispose();
        itemTileAtlas.dispose();
        uiRenderer.dispose();
        itemRenderer.dispose();
        screenTransition.dispose();
    }


    private void changeLevel() {
        // Generate a completely new dungeon.
        gameMap = mapGenerator.generate(100, 100);

        // Populate the new dungeon.
        enemies = enemySpawner.spawnEnemies(
            gameMap,
            mapGenerator.getRooms()
        );
        itemSpawner.spawnItems(gameMap);

        // Move the existing player to the new dungeon.
        Cell spawnPoint = gameMap.getRandomWalkableCell();
        player.setPosition(
            spawnPoint.getPosition().x(),
            spawnPoint.getPosition().y()
        );

        // The old controller references the previous map.
        playerController = new PlayerController(player, gameMap);

        // Re-initialize field of view for the new map
        visibilityMap = new VisibilityMap(
            gameMap.getWidth(),
            gameMap.getHeight()
        );

        fieldOfView = new FieldOfView(
            gameMap,
            visibilityMap,
            player
        );
        visibilityMask = new VisibilityMask(gameMap);
        visibilityMask.update(player);

        // updates the renderer's visibility map
        mapRenderer.setVisibilityMap(visibilityMap);
        mapRenderer.setVisibilityMask(visibilityMask);

        // Center the camera on the new player position immediately.
        cameraController.update(
            player.getX(),
            player.getY(),
            gameMap.getHeight()
        );


        uiRenderer.showNotification("You entered a new dungeon!");
    }

}
