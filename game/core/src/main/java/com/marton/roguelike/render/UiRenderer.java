package com.marton.roguelike.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marton.roguelike.entity.Player;

public class UiRenderer {

    private final SpriteBatch batch;
    private final BitmapFont font;

    public UiRenderer() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    public void render(Player player) {
        batch.begin();
        font.draw(batch, getHealthText(player), 20, 30);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    private String getHealthText(Player player) {
        return player.getCurrentHealth() + "/" + player.getMaxHealth();
    }
}
