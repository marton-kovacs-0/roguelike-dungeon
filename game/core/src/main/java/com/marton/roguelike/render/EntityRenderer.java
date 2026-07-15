package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.world.EntityType;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class EntityRenderer {

    private final EntityTileAtlas entityTileAtlas;
    private final SpriteBatch batch;

    public EntityRenderer(EntityTileAtlas entityTileAtlas) {
        this.batch = new SpriteBatch();
        this.entityTileAtlas = entityTileAtlas;
    }

    public void render(GameMap gameMap, Player player, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderPlayer(player, gameMap.getHeight());
        batch.end();
    }

    private void renderPlayer(Player player, int mapHeight) {
        drawEntity(
            entityTileAtlas.getRegion(EntityType.PLAYER),
            player.getX(),
            toRenderY(player.getY(), mapHeight)
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
