package com.marton.roguelike.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import static com.marton.roguelike.render.CoordinateUtils.toRenderY;
import static com.marton.roguelike.render.RenderConstants.RENDER_TILE_SIZE;

public class CameraController extends InputAdapter {
    private final OrthographicCamera camera;

    private static final float ZOOM_SPEED = 0.1f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 100f;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * ZOOM_SPEED;
        camera.zoom = Math.max(
            MIN_ZOOM,
            Math.min(MAX_ZOOM, camera.zoom)
        );
        return true;
    }

    public void update(float targetX, float targetY, int gameMapHeight) {
        camera.position.set(
            targetX * RENDER_TILE_SIZE + RENDER_TILE_SIZE / 2f,
            toRenderY(targetY, gameMapHeight) * RENDER_TILE_SIZE + RENDER_TILE_SIZE / 2f,
            0
        );
        camera.update();
    }
}
