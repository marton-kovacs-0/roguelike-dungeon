package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.item.WorldItem;
import com.marton.roguelike.render.atlas.ItemTileAtlas;
import com.marton.roguelike.world.GameMap;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;
import static com.marton.roguelike.render.CoordinateUtils.toRenderY;



public class ItemRenderer {
    private final ItemTileAtlas itemTileAtlas;
    private final SpriteBatch batch;

    public ItemRenderer(ItemTileAtlas itemTileAtlas) {
        this.itemTileAtlas = itemTileAtlas;
        this.batch = new SpriteBatch();
    }

    public void render(GameMap gameMap, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (WorldItem item : gameMap.getWorldItems()) {
            drawItem(
                itemTileAtlas.getRegion(item.getItem().getItemType()),
                item.getPosition().x(),
                toRenderY(item.getPosition().y(), gameMap.getHeight()));
        }
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

    public void dispose() {
        batch.dispose();
    }
}
