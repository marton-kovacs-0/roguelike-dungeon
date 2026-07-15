package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.world.EntityType;
import com.marton.roguelike.world.GameMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

import java.util.List;

public class EntityRenderer {

    private final EntityTileAtlas entityTileAtlas;
    private final SpriteBatch batch;

    public EntityRenderer(EntityTileAtlas entityTileAtlas) {
        this.batch = new SpriteBatch();
        this.entityTileAtlas = entityTileAtlas;
    }

    public void render(GameMap gameMap, Player player, List<Enemy> enemies, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderPlayer(player, gameMap.getHeight());
        renderEnemies(enemies, gameMap.getHeight());
        batch.end();
    }

    private void renderPlayer(Player player, int mapHeight) {
        drawEntity(
            entityTileAtlas.getRegion(EntityType.PLAYER),
            player.getX(),
            toRenderY(player.getY(), mapHeight)
        );
    }

    // Renders all enemies currently present on the map.
    private void renderEnemies(List<Enemy> enemies, int mapHeight) {
        for (Enemy enemy : enemies) {
            drawEntity(
                entityTileAtlas.getRegion(enemy.getEntityType()),
                enemy.getX(), toRenderY(enemy.getY(), mapHeight));
        }
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
