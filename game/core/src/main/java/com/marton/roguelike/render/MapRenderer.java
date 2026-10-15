package com.marton.roguelike.render;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.render.atlas.MapTileAtlas;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.visibility.VisibilityMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class MapRenderer {

    private final SpriteBatch batch;
    private final MapTileAtlas mapTileAtlas;
    private VisibilityMap visibilityMap;
    private final Texture blackTexture;


    public MapRenderer(MapTileAtlas mapTileAtlas, VisibilityMap visibilityMap) {
        this.batch = new SpriteBatch();
        this.mapTileAtlas = mapTileAtlas;
        this.visibilityMap = visibilityMap;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 1f);
        pixmap.fill();

        blackTexture = new Texture(pixmap);
        pixmap.dispose();
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
                boolean visible = visibilityMap.isCellVisible(cell.getPosition());
                boolean explored = visibilityMap.isCellExplored(cell.getPosition());

                if (visible) {
                    drawTile(mapTileAtlas.getRegion(cell.getCellType()), x, toRenderY(y, height));
                } else if (explored) {
                    batch.setColor(0.4f, 0.4f, 0.4f, 1f);

                    drawTile(
                        mapTileAtlas.getRegion(cell.getCellType()),
                        x,
                        toRenderY(y, height)
                    );

                    // reverses color settings so that the next tile does not inherit it
                    batch.setColor(1f, 1f, 1f, 1f);
                } else {
                    drawBlackTile(x, toRenderY(y, height));
                }

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

    private void drawBlackTile(int tileX, int tileY) {
        batch.draw(
            blackTexture,
            tileX * RENDER_TILE_SIZE,
            tileY * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );
    }

    public void setVisibilityMap(VisibilityMap visibilityMap) {
        this.visibilityMap = visibilityMap;
    }

    public void dispose() {
        batch.dispose();
        blackTexture.dispose();
    }
}
