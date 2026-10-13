
package com.marton.roguelike.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.marton.roguelike.entity.Player;
import com.marton.roguelike.input.PlayerController;
import com.marton.roguelike.item.Item;
import com.marton.roguelike.item.ItemType;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class UiRenderer {

    private final SpriteBatch batch;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;

    private String notification = "";
    private float notificationTimer = 0f;

    private static final float NOTIFICATION_DURATION = 2f;

    // Inventory panel constants
    private static final float PANEL_X = 20;
    private static final float PANEL_Y = 50;
    private static final float PANEL_WIDTH = 260;

    private static final float ROW_HEIGHT = 25;
    private static final float BUTTON_SIZE = 30;
    private static final float BUTTON_GAP = 5;
    private static final float DROP_BUTTON_WIDTH = 45;

    public UiRenderer() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();

        batch.getProjectionMatrix().setToOrtho2D(
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        shapeRenderer.setProjectionMatrix(
            batch.getProjectionMatrix()
        );
    }

    public void render(Player player, boolean inventoryOpen, float deltaTime) {
        if (notificationTimer > 0f) {
            notificationTimer -= deltaTime;

            if (notificationTimer <= 0f) {
                notification = "";
            }
        }

        batch.begin();

        font.draw(batch, getHealthText(player), 20, 30);

        if (!notification.isEmpty()) {
            font.draw(
                batch,
                notification,
                20,
                Gdx.graphics.getHeight() - 30
            );
        }

        batch.end();

        if (inventoryOpen) {
            renderInventory(player.getInventory());
        }
    }


    private void renderInventory(List<Item> inventory) {
        float panelHeight = 50 + inventory.size() * ROW_HEIGHT;

        float dropX = PANEL_X + PANEL_WIDTH
            - DROP_BUTTON_WIDTH - BUTTON_GAP;
        float deleteX = dropX - BUTTON_GAP - BUTTON_SIZE;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(PANEL_X, PANEL_Y, PANEL_WIDTH, panelHeight);

        float textY = PANEL_Y + panelHeight - 25;

        for (Item item : inventory) {
            textY -= ROW_HEIGHT;
            float buttonY = textY - 18;

            // Delete button: keys cannot be deleted.
            if (item.getItemType() != ItemType.KEY) {
                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.rect(
                    deleteX, buttonY, BUTTON_SIZE, 20
                );
            }

            // Drop button
            shapeRenderer.setColor(Color.FOREST);
            shapeRenderer.rect(
                dropX, buttonY, DROP_BUTTON_WIDTH, 20
            );
        }

        shapeRenderer.end();

        batch.begin();

        textY = PANEL_Y + panelHeight - 25;
        font.draw(
            batch,
            "Inventory (" + inventory.size() + "/10)",
            PANEL_X + 15,
            textY
        );

        for (Item item : inventory) {
            textY -= ROW_HEIGHT;

            font.draw(
                batch,
                item.getName(),
                PANEL_X + 15,
                textY
            );

            float buttonY = textY - 18;

            if (item.getItemType() != ItemType.KEY) {
                font.draw(
                    batch,
                    "X",
                    deleteX + 11,
                    buttonY + 14
                );
            }

            font.draw(
                batch,
                "Drop",
                dropX + 5,
                buttonY + 14
            );
        }

        batch.end();
    }


    private Rectangle getDeleteButtonBounds(float textY) {
        float dropX = PANEL_X + PANEL_WIDTH
            - DROP_BUTTON_WIDTH - BUTTON_GAP;
        float deleteX = dropX - BUTTON_GAP - BUTTON_SIZE;
        float buttonY = textY - 18;

        return new Rectangle(
            deleteX, buttonY, BUTTON_SIZE, 20
        );
    }

    private Rectangle getDropButtonBounds(float textY) {
        float dropX = PANEL_X + PANEL_WIDTH
            - DROP_BUTTON_WIDTH - BUTTON_GAP;
        float buttonY = textY - 18;

        return new Rectangle(
            dropX, buttonY, DROP_BUTTON_WIDTH, 20
        );
    }

    private Vector2 getMousePosition() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return new Vector2(mouseX, mouseY);
    }

    public void handleButtonClick(List<Item> inventory, PlayerController controller) {
        float panelHeight = 50 + inventory.size() * ROW_HEIGHT;
        float textY = PANEL_Y + panelHeight - 25;


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 mousePosition = getMousePosition();

            for (int i = 0; i < inventory.size(); i++) {
                textY -= ROW_HEIGHT;

                if (inventory.get(i).getItemType() == ItemType.KEY) {
                    Rectangle drop = getDropButtonBounds(textY);
                    if (drop.contains(mousePosition)) {
                        controller.dropItemFromInventory(i);
                        break;
                    }
                } else {
                    Rectangle delete = getDeleteButtonBounds(textY);
                    Rectangle drop = getDropButtonBounds(textY);
                    if (delete.contains(mousePosition)) {
                        controller.deleteItemFromInventory(i);
                        break;
                    } else if (drop.contains(mousePosition)) {
                        controller.dropItemFromInventory(i);
                        break;
                    }
                }
            }
        }


    }

    public void showNotification(String message) {
        notification = message;
        notificationTimer = NOTIFICATION_DURATION;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }

    private String getHealthText(Player player) {
        return player.getCurrentHealth() + "/" + player.getMaxHealth();
    }
}
