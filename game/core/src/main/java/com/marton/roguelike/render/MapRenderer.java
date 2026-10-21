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
import com.marton.roguelike.world.visibility.VisibilityMask;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class MapRenderer {

    private final SpriteBatch batch;
    private final MapTileAtlas mapTileAtlas;
    private VisibilityMap visibilityMap;
    private final Texture blackTexture;
    private VisibilityMask visibilityMask;


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

    public void render(
        GameMap gameMap,
        OrthographicCamera camera,
        boolean debugMapVisible) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderMap(gameMap, debugMapVisible);
        batch.end();
    }



    private void renderMap(GameMap gameMap, boolean debugMapVisible) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameMap.getCell(x, y);
                int renderY = toRenderY(y, height);

                TextureRegion region =
                    mapTileAtlas.getRegion(cell.getCellType());

                // Debug mode reveals the entire map without applying visibility.
                if (debugMapVisible) {
                    batch.setColor(1f, 1f, 1f, 1f);
                    drawTile(region, x, renderY);
                    continue;
                }

                boolean visible =
                    visibilityMap.isCellVisible(cell.getPosition());

                boolean explored =
                    visibilityMap.isCellExplored(cell.getPosition());

                if (visible) {
                    drawTileWithVisibility(region, x, y, height);

                } else if (explored) {
                    batch.setColor(0.4f, 0.4f, 0.4f, 1f);
                    drawTile(region, x, renderY);
                    batch.setColor(1f, 1f, 1f, 1f);

                } else {
                    batch.setColor(1f, 1f, 1f, 1f);
                    drawBlackTile(x, renderY);
                }
            }
        }

        batch.setColor(1f, 1f, 1f, 1f);
    }






    private void drawVisibilityOverlay(int tileX, int tileY, int mapHeight) {
        float sampleSize = (float) RENDER_TILE_SIZE / 4;

        for (int sampleX = 0; sampleX < 4; sampleX++) {
            for (int sampleY = 0; sampleY < 4; sampleY++) {
                float lightLevel = visibilityMask.getLightLevel(
                    tileX,
                    tileY,
                    sampleX,
                    sampleY
                );

                float alpha = 1.0f - lightLevel;

                if (alpha <= 0.0f) {
                    continue;
                }

                batch.setColor(0f, 0f, 0f, alpha);

                float renderX = tileX * RENDER_TILE_SIZE
                    + sampleX * sampleSize;

                float renderY = toRenderY(tileY, mapHeight)
                    + (3 - sampleY) * sampleSize;

                batch.draw(
                    blackTexture,
                    renderX,
                    renderY,
                    sampleSize,
                    sampleSize
                );
            }
        }

        batch.setColor(1f, 1f, 1f, 1f);
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



    private void drawTilePart(
        TextureRegion region,
        int tileX,
        int tileY,
        int sampleX,
        int sampleY,
        int mapHeight
    ) {
        float sampleSize = (float) RENDER_TILE_SIZE / 4;

        float u0 = region.getU();
        float u1 = region.getU2();
        float v0 = region.getV();
        float v1 = region.getV2();

        float partU0 = u0 + (u1 - u0) * sampleX / 4f;
        float partU1 = u0 + (u1 - u0) * (sampleX + 1) / 4f;

        // TextureRegion's V coordinates run opposite to screen-space Y.
        float partV0 = v0 + (v1 - v0) * sampleY / 4f;
        float partV1 = v0 + (v1 - v0) * (sampleY + 1) / 4f;

        TextureRegion sampleRegion = new TextureRegion(
            region.getTexture(),
            partU0,
            partV0,
            partU1,
            partV1
        );

        float renderX = tileX * RENDER_TILE_SIZE
            + sampleX * sampleSize;

        float renderY = toRenderY(tileY, mapHeight)
            + (3 - sampleY) * sampleSize;

        batch.draw(
            sampleRegion,
            renderX,
            renderY,
            sampleSize,
            sampleSize
        );
    }




    private void drawTileWithVisibility(
        TextureRegion region,
        int tileX,
        int tileY,
        int mapHeight
    ) {
        float sampleSize = (float) RENDER_TILE_SIZE / 4;

        for (int sampleX = 0; sampleX < 4; sampleX++) {
            for (int sampleY = 0; sampleY < 4; sampleY++) {
                float lightLevel = visibilityMask.getLightLevel(
                    tileX,
                    tileY,
                    sampleX,
                    sampleY
                );

                // Draw the original texture section first.
                drawTilePart(
                    region,
                    tileX,
                    tileY,
                    sampleX,
                    sampleY,
                    mapHeight
                );

                // Darken the section according to its light level.
                float darkness = 1f - lightLevel;

                if (darkness > 0f) {
                    batch.setColor(0f, 0f, 0f, darkness);

                    float renderX = tileX * RENDER_TILE_SIZE
                        + sampleX * sampleSize;

                    float renderY = toRenderY(tileY, mapHeight)
                        + (3 - sampleY) * sampleSize;

                    batch.draw(
                        blackTexture,
                        renderX,
                        renderY,
                        sampleSize,
                        sampleSize
                    );

                    batch.setColor(1f, 1f, 1f, 1f);
                }
            }
        }
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

    public void setVisibilityMask(VisibilityMask visibilityMask) {
        this.visibilityMask = visibilityMask;
    }

    public void dispose() {
        batch.dispose();
        blackTexture.dispose();
    }
}
