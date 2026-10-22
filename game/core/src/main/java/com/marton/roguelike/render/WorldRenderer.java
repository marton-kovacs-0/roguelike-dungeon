package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.entity.enemy.Enemy;
import com.marton.roguelike.render.atlas.EntityTileAtlas;
import com.marton.roguelike.render.atlas.ItemTileAtlas;
import com.marton.roguelike.render.atlas.MapTileAtlas;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.visibility.VisibilityMap;

import java.util.List;

public class WorldRenderer {

    private final MapRenderer mapRenderer;
    private final EntityRenderer entityRenderer;
    private final ItemRenderer itemRenderer;

    public WorldRenderer(
        MapTileAtlas mapTileAtlas,
        EntityTileAtlas entityTileAtlas,
        ItemTileAtlas itemTileAtlas,
        VisibilityMap visibilityMap) {

        this.mapRenderer = new MapRenderer(mapTileAtlas, visibilityMap);
        this.entityRenderer = new EntityRenderer(entityTileAtlas, visibilityMap);
        this.itemRenderer = new ItemRenderer(itemTileAtlas, visibilityMap);
    }

    public void render(
        GameMap gameMap,
        Player player,
        List<Enemy> enemies,
        OrthographicCamera camera,
        boolean debugMode) {

        mapRenderer.render(gameMap, camera, debugMode);
        entityRenderer.render(gameMap, player, enemies, camera, debugMode);
        itemRenderer.render(gameMap, camera, debugMode);
    }

    public void setVisibilityMap(VisibilityMap visibilityMap) {
        mapRenderer.setVisibilityMap(visibilityMap);
        entityRenderer.setVisibilityMap(visibilityMap);
        itemRenderer.setVisibilityMap(visibilityMap);
    }

    public void dispose() {
        mapRenderer.dispose();
        entityRenderer.dispose();
        itemRenderer.dispose();
    }
}
