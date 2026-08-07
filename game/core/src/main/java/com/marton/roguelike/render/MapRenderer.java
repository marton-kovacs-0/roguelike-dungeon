package com.marton.roguelike.render;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.render.atlas.MapTileAtlas;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class MapRenderer {

    private final SpriteBatch batch;
    private final MapTileAtlas mapTileAtlas;




    public MapRenderer(MapTileAtlas mapTileAtlas) {
        this.batch = new SpriteBatch();
        this.mapTileAtlas = mapTileAtlas;
    }

    public void render(GameMap gameMap,OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderMap(gameMap);
        batch.end();
    }

    private void renderMap(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameMap.getCell(x, y);
                drawTile(mapTileAtlas.getRegion(cell.getCellType()), x, toRenderY(y, height));
            }
        }
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


    public void dispose() {
        batch.dispose();
    }
}
