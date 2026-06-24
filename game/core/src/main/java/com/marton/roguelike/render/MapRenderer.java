package com.marton.roguelike.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;

public class MapRenderer {

    private final SpriteBatch batch;
    private final TileAtlas atlas;
    private final OrthographicCamera camera;

    private static final int RENDER_SCALE = 3;
    private static final int SOURCE_TILE_SIZE = 16;
    private static final int RENDER_TILE_SIZE = RENDER_SCALE * SOURCE_TILE_SIZE;


    public MapRenderer(TileAtlas atlas) {
        this.batch = new SpriteBatch();
        this.atlas = atlas;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(
            false,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }

    public void render(GameMap gameMap, Player player) {
        camera.position.set(
            player.getX() * RENDER_TILE_SIZE + RENDER_TILE_SIZE / 2f,
            toRenderY(player.getY(), gameMap.getHeight()) * RENDER_TILE_SIZE + RENDER_TILE_SIZE / 2f,
            0
        );
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderMap(gameMap);
        renderPlayer(player, gameMap.getHeight());
        batch.end();
    }

    private void renderMap(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameMap.getCell(x, y);
                drawTile(atlas.getRegionForType(cell.getCellType()), x, toRenderY(y, height));
            }
        }
    }

    private void renderPlayer(Player player, int mapHeight) {
        drawEntity(
            atlas.getPlayerTile(),
            player.getX(),
            toRenderY(player.getY(), mapHeight)
        );
    }

    private int toRenderY(int gameY, int mapHeight) {
        return mapHeight - 1 - gameY;
    }

    private float toRenderY(float gameY, int mapHeight) {
        return mapHeight - 1 -gameY;
    }

    private void drawTile(TextureRegion region, int tileX, int tileY) {
        batch.draw(
            region,
            tileX * RENDER_TILE_SIZE,
            tileY * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );
    }

    private void drawEntity(TextureRegion region, float x, float y) {
        batch.draw(
            region,
            x * RENDER_TILE_SIZE,
            y * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );
    }

    public void dispose() {
        batch.dispose();
    }
}
