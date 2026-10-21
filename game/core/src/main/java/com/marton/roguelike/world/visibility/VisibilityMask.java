package com.marton.roguelike.world.visibility;

import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.entity.Player;

public class VisibilityMask {

    private static final int SAMPLES_PER_TILE = 4;
    private static final float VISION_RADIUS = 8.0f;
    private static final float FADE_DISTANCE = 2.0f;

    private final GameMap gameMap;
    private final float[][][][] lightLevels;

    public VisibilityMask(GameMap gameMap) {
        this.gameMap = gameMap;
        this.lightLevels = new float
            [gameMap.getWidth()]
            [gameMap.getHeight()]
            [SAMPLES_PER_TILE]
            [SAMPLES_PER_TILE];
    }


    public void update(Player player) {
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                for (int sampleX = 0; sampleX < SAMPLES_PER_TILE; sampleX++) {
                    for (int sampleY = 0; sampleY < SAMPLES_PER_TILE; sampleY++) {
                        lightLevels[x][y][sampleX][sampleY] = 0.0f;
                    }
                }
            }
        }


        float playerX = (float) Math.floor(player.getX()) + 0.5f;
        float playerY = (float) Math.floor(player.getY()) + 0.5f;

        int minX = Math.max(
            0,
            (int) Math.floor(playerX - VISION_RADIUS)
        );
        int maxX = Math.min(
            gameMap.getWidth() - 1,
            (int) Math.floor(playerX + VISION_RADIUS)
        );

        int minY = Math.max(
            0,
            (int) Math.floor(playerY - VISION_RADIUS)
        );
        int maxY = Math.min(
            gameMap.getHeight() - 1,
            (int) Math.floor(playerY + VISION_RADIUS)
        );

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int sampleX = 0; sampleX < SAMPLES_PER_TILE; sampleX++) {
                    for (int sampleY = 0; sampleY < SAMPLES_PER_TILE; sampleY++) {

                        float worldX = getSampleWorldX(x, sampleX);
                        float worldY = getSampleWorldY(y, sampleY);

                        float distanceX = worldX - playerX;
                        float distanceY = worldY - playerY;

                        float distance = (float) Math.sqrt(
                            distanceX * distanceX + distanceY * distanceY
                        );

                        if (distance > VISION_RADIUS) {
                            continue;
                        }

                        boolean visible = hasLineOfSight(
                            playerX, playerY, worldX, worldY
                        );

                        if (visible) {
                            lightLevels[x][y][sampleX][sampleY] =
                                calculateLightLevel(distance);
                        }
                    }
                }
            }
        }
    }


    private float getSampleOffset(int sampleIndex) {
        return (sampleIndex + 0.5f) / SAMPLES_PER_TILE;
    }
    private float getSampleWorldX(int tileX, int sampleX) {
        return tileX + getSampleOffset(sampleX);
    }

    private float getSampleWorldY(int tileY, int sampleY) {
        return tileY + getSampleOffset(sampleY);
    }

    private boolean blocksVision(int x, int y) {
        if (!gameMap.isInsideMap(x, y)) {
            return true;
        }

        Cell cell = gameMap.getCell(x, y);
        return cell.getCellType().blocksVision();
    }

    public float getLightLevel(
        int tileX,
        int tileY,
        int sampleX,
        int sampleY) {

        return lightLevels[tileX][tileY][sampleX][sampleY];
    }

    private float calculateLightLevel(float distance) {
        float fadeStart = VISION_RADIUS - FADE_DISTANCE;

        if (distance <= fadeStart) {
            return 1.0f;
        }

        return (VISION_RADIUS - distance) / FADE_DISTANCE;
    }

    private boolean hasLineOfSight(
        float startX,
        float startY,
        float targetX,
        float targetY) {

        int currentX = (int) Math.floor(startX);
        int currentY = (int) Math.floor(startY);

        int targetCellX = (int) Math.floor(targetX);
        int targetCellY = (int) Math.floor(targetY);

        double deltaX = targetX - startX;
        double deltaY = targetY - startY;

        int stepX = Double.compare(deltaX, 0.0);
        int stepY = Double.compare(deltaY, 0.0);

        double tDeltaX = deltaX == 0.0
            ? Double.POSITIVE_INFINITY
            : 1.0 / Math.abs(deltaX);

        double tDeltaY = deltaY == 0.0
            ? Double.POSITIVE_INFINITY
            : 1.0 / Math.abs(deltaY);

        double tMaxX = deltaX == 0.0
            ? Double.POSITIVE_INFINITY
            : ((stepX > 0 ? currentX + 1.0 : currentX) - startX)
            / deltaX;

        double tMaxY = deltaY == 0.0
            ? Double.POSITIVE_INFINITY
            : ((stepY > 0 ? currentY + 1.0 : currentY) - startY)
            / deltaY;

        while (currentX != targetCellX || currentY != targetCellY) {

            if (Math.abs(tMaxX - tMaxY) < 1e-10) {
                if (blocksVision(currentX + stepX, currentY)
                    || blocksVision(currentX, currentY + stepY)) {
                    return false;
                }

                currentX += stepX;
                currentY += stepY;

                tMaxX += tDeltaX;
                tMaxY += tDeltaY;

            } else if (tMaxX < tMaxY) {
                currentX += stepX;
                tMaxX += tDeltaX;

            } else {
                currentY += stepY;
                tMaxY += tDeltaY;
            }

            // The targeted cell is considered visible even if it is a wall.
            if (currentX == targetCellX && currentY == targetCellY) {
                return true;
            }

            if (blocksVision(currentX, currentY)) {
                return false;
            }
        }

        return true;
    }
}
