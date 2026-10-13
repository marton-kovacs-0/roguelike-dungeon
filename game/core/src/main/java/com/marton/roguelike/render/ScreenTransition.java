
package com.marton.roguelike.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.GL20;

public class ScreenTransition {

    private enum State {
        IDLE,
        FADE_OUT,
        WAITING_FOR_LEVEL_CHANGE,
        FADE_IN
    }

    private static final float TRANSITION_DURATION = 1.2f;

    private final ShapeRenderer shapeRenderer;
    private State state = State.IDLE;
    private float alpha = 0f;

    public ScreenTransition() {
        shapeRenderer = new ShapeRenderer();
    }

    public void start() {
        if (state == State.IDLE) {
            state = State.FADE_OUT;
            alpha = 0f;
        }
    }

    public void update(float deltaTime) {
        if (state == State.FADE_OUT) {
            alpha = Math.min(1f, alpha + deltaTime / TRANSITION_DURATION);

            if (alpha >= 1f) {
                state = State.WAITING_FOR_LEVEL_CHANGE;
            }
        } else if (state == State.FADE_IN) {
            alpha = Math.max(0f, alpha - deltaTime / TRANSITION_DURATION);

            if (alpha <= 0f) {
                state = State.IDLE;
            }
        }
    }

    public boolean isWaitingForLevelChange() {
        return state == State.WAITING_FOR_LEVEL_CHANGE;
    }

    public void startFadeIn() {
        if (state == State.WAITING_FOR_LEVEL_CHANGE) {
            state = State.FADE_IN;
        }
    }

    public boolean isActive() {
        return state != State.IDLE;
    }


    public void render() {
        if (alpha <= 0f) {
            return;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shapeRenderer.setProjectionMatrix(
            new Matrix4().setToOrtho2D(
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            )
        );

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, alpha);
        shapeRenderer.rect(
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    public void dispose() {
        shapeRenderer.dispose();
    }
}
