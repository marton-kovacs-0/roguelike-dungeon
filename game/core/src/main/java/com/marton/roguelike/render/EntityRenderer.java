package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.world.EntityType;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;
import com.marton.roguelike.world.visibility.VisibilityMap;

import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

import java.util.List;

public class EntityRenderer {

    private final EntityTileAtlas entityTileAtlas;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private VisibilityMap visibilityMap;

    private static final int HEALTH_TEXT_OFFSET_Y = 15;
    private static final int HEALTH_TEXT_OFFSET_X = 1;

    public EntityRenderer(EntityTileAtlas entityTileAtlas, VisibilityMap visibilityMap) {
        this.batch = new SpriteBatch();
        this.entityTileAtlas = entityTileAtlas;
        this.font = new BitmapFont();
        this.visibilityMap = visibilityMap;
    }

    public void render(
        GameMap gameMap,
        Player player,
        List<Enemy> enemies,
        OrthographicCamera camera,
        boolean debugMode
    ) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderPlayer(player, gameMap.getHeight());
        renderEnemies(enemies, gameMap.getHeight(), debugMode);
        batch.end();
    }

    private void renderPlayer(Player player, int mapHeight) {
        drawEntity(
            entityTileAtlas.getRegion(EntityType.PLAYER),
            player.getX(),
            toRenderY(player.getY(), mapHeight),
            player.isFlashing()
        );
    }

    // Renders all enemies currently present on the map.
    private void renderEnemies(List<Enemy> enemies, int mapHeight, boolean debugMode) {
        for (Enemy enemy : enemies) {
            int tileX = (int) Math.floor(enemy.getX());
            int tileY = (int) Math.floor(enemy.getY());

            if (!debugMode && !visibilityMap.isCellVisible(new TilePosition(tileX, tileY))) {
                continue;
            }

            drawEntity(
                entityTileAtlas.getRegion(enemy.getEntityType()),
                enemy.getX(),
                toRenderY(enemy.getY(), mapHeight),
                enemy.isFlashing()
            );

            drawEnemyHealth(enemy, mapHeight);
        }
    }

    private void drawEntity(TextureRegion region, float x, float y, boolean isFlashing) {
        if (isFlashing) {
            batch.setColor(Color.RED);
        } else {
            batch.setColor(Color.WHITE);
        }

        batch.draw(
            region,
            x * RENDER_TILE_SIZE,
            y * RENDER_TILE_SIZE,
            RENDER_TILE_SIZE,
            RENDER_TILE_SIZE
        );

        batch.setColor(Color.WHITE);
    }

    private void drawEnemyHealth(Enemy enemy, int mapHeight) {
        font.draw(
            batch,
            getEnemyHealthText(enemy),
            enemy.getX() * RENDER_TILE_SIZE + HEALTH_TEXT_OFFSET_X,
            toRenderY(enemy.getY(), mapHeight) * RENDER_TILE_SIZE + RENDER_TILE_SIZE + HEALTH_TEXT_OFFSET_Y
        );
    }

    private String getEnemyHealthText(Enemy enemy) {
        return enemy.getCurrentHealth() + "/" + enemy.getMaxHealth();
    }

    public void setVisibilityMap(VisibilityMap visibilityMap) {
        this.visibilityMap = visibilityMap;
    }

    public void dispose() {
        batch.dispose();
    }
}
