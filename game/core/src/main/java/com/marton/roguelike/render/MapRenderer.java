package com.marton.roguelike.render;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class MapRenderer {

    private final SpriteBatch batch;
    private final TileAtlas atlas;
    private final OrthographicCamera camera;



    public MapRenderer(TileAtlas atlas, OrthographicCamera camera) {
        this.batch = new SpriteBatch();
        this.atlas = atlas;
        this.camera = camera;
    }

    public void render(GameMap gameMap, Player player) {
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
