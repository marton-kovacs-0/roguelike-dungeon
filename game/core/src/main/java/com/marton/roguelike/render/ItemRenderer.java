package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.item.WorldItem;
import com.marton.roguelike.render.atlas.ItemTileAtlas;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.visibility.VisibilityMap;

import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;
import static com.marton.roguelike.render.CoordinateUtils.toRenderY;



public class ItemRenderer {
    private final ItemTileAtlas itemTileAtlas;
    private VisibilityMap visibilityMap;
    private final SpriteBatch batch;

    public ItemRenderer(ItemTileAtlas itemTileAtlas, VisibilityMap visibilityMap) {
        this.itemTileAtlas = itemTileAtlas;
        this.visibilityMap = visibilityMap;
        this.batch = new SpriteBatch();
    }


    public void render(GameMap gameMap, OrthographicCamera camera, boolean debugMode) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (WorldItem item : gameMap.getWorldItems()) {
            TilePosition position = item.getPosition();

            boolean visible = visibilityMap.isCellVisible(position);
            boolean explored = visibilityMap.isCellExplored(position);

            if (!debugMode && !visible && !explored) {
                continue;
            }

            TextureRegion region =
                itemTileAtlas.getRegion(item.getItem().getItemType());

            if (debugMode || visible) {
                batch.setColor(1f, 1f, 1f, 1f);
            } else {
                batch.setColor(0.4f, 0.4f, 0.4f, 1f);
            }

            drawItem(
                region,
                position.x(),
                toRenderY(position.y(), gameMap.getHeight())
            );
        }

        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
    }


    private void drawItem(TextureRegion region, int x, int y) {
        batch.draw(
            region,
            x * RENDER_TILE_SIZE,
            y * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );
    }

    public void setVisibilityMap(VisibilityMap visibilityMap) {
        this.visibilityMap = visibilityMap;
    }

    public void dispose() {
        batch.dispose();
    }
}
