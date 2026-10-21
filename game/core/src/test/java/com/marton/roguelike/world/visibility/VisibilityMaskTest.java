package com.marton.roguelike.world.visibility;

import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.TilePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VisibilityMaskTest {

    private static final float DELTA = 0.0001f;

    private VisibilityMask visibilityMask;
    private GameMap gameMap;
    private Player player;

    @BeforeEach
    void setUp() {
        gameMap = createTestMap();
        player = new Player(12, 12, 100);
        visibilityMask = new VisibilityMask(gameMap);
    }

    private GameMap createTestMap() {
        int width = 25;
        int height = 25;

        GameMap gameMap = new GameMap(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TilePosition position = new TilePosition(x, y);

                MapCellType cellType;

                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    cellType = MapCellType.WALL;
                } else {
                    cellType = MapCellType.FLOOR;
                }

                gameMap.setCell(position, new Cell(position, cellType));
            }
        }

        return gameMap;
    }

    @Test
    void playerTileSamplesShouldBeVisible() {
        visibilityMask.update(player);

        for (int sampleX = 0; sampleX < 4; sampleX++) {
            for (int sampleY = 0; sampleY < 4; sampleY++) {
                assertEquals(
                    1.0f,
                    visibilityMask.getLightLevel(12, 12, sampleX, sampleY),
                    DELTA,
                    "All samples in the player's tile should be visible"
                );
            }
        }
    }

    @Test
    void openAreaSamplesWithinRadiusShouldBeVisible() {
        visibilityMask.update(player);

        assertTrue(
            visibilityMask.getLightLevel(13, 12, 1, 1) > 0.0f,
            "A sample in an adjacent open tile should be visible"
        );

        assertTrue(
            visibilityMask.getLightLevel(14, 12, 1, 1) > 0.0f,
            "A sample in an open tile two steps away should be visible"
        );
    }

    @Test
    void wallShouldBeVisibleButSamplesBehindItShouldNotBeVisible() {
        TilePosition wallPosition = new TilePosition(13, 12);

        gameMap.setCell(
            wallPosition,
            new Cell(wallPosition, MapCellType.WALL)
        );

        visibilityMask.update(player);

        assertTrue(
            visibilityMask.getLightLevel(13, 12, 1, 1) > 0.0f,
            "The wall itself should remain visible"
        );

        assertEquals(
            0.0f,
            visibilityMask.getLightLevel(14, 12, 1, 1),
            DELTA,
            "A sample directly behind the wall should not be visible"
        );
    }

    @Test
    void samplesOutsideNewVisionRadiusShouldBeClearedAfterPlayerMoves() {
        visibilityMask.update(player);

        // Verify that a sample is initially illuminated.
        assertTrue(
            visibilityMask.getLightLevel(10, 10, 1, 1) > 0.0f,
            "Sample should initially be illuminated"
        );

        // Move the player far enough for the sample to leave the vision radius.
        player.setPosition(20, 20);
        visibilityMask.update(player);

        assertEquals(
            0.0f,
            visibilityMask.getLightLevel(10, 10, 1, 1),
            DELTA,
            "Samples outside the new vision area should be cleared"
        );
    }

    @Test
    void samplesOutsideVisionRadiusShouldNotBeIlluminated() {
        visibilityMask.update(player);

        // The player is at (12.5, 12.5) for visibility calculations.
        // This sample is well within the vision radius.
        assertTrue(
            visibilityMask.getLightLevel(19, 12, 1, 1) > 0.0f,
            "Sample inside the vision radius should be illuminated"
        );

        // This sample is outside the vision radius.
        assertEquals(
            0.0f,
            visibilityMask.getLightLevel(21, 12, 1, 1),
            DELTA,
            "Sample outside the vision radius should not be illuminated"
        );
    }

    @Test
    void lightShouldNotPassDiagonallyBetweenTwoBlockingWalls() {
        TilePosition horizontalWall = new TilePosition(13, 12);
        TilePosition verticalWall = new TilePosition(12, 13);

        gameMap.setCell(
            horizontalWall,
            new Cell(horizontalWall, MapCellType.WALL)
        );

        gameMap.setCell(
            verticalWall,
            new Cell(verticalWall, MapCellType.WALL)
        );

        visibilityMask.update(player);

        assertEquals(
            0.0f,
            visibilityMask.getLightLevel(13, 13, 1, 1),
            DELTA,
            "Light should not pass diagonally between two blocking walls"
        );
    }

    @Test
    void samplesInsideFadeZoneShouldHaveReducedLightLevel() {
        visibilityMask.update(player);

        float lightLevel = visibilityMask.getLightLevel(19, 12, 1, 1);

        assertTrue(
            lightLevel > 0.0f && lightLevel < 1.0f,
            "Samples in the fade zone should have reduced light levels"
        );
    }

    @Test
    void samplesWellInsideVisionRadiusShouldHaveFullLightLevel() {
        visibilityMask.update(player);

        assertEquals(
            1.0f,
            visibilityMask.getLightLevel(14, 12, 1, 1),
            DELTA,
            "Samples well inside the vision radius should have full light"
        );
    }
}
