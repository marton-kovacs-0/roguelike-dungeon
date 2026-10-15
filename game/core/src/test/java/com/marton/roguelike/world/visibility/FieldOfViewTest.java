package com.marton.roguelike.world.visibility;

import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.Cell;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.MapCellType;
import com.marton.roguelike.world.TilePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldOfViewTest {

    private VisibilityMap visibilityMap;
    private FieldOfView fieldOfView;
    private GameMap gameMap;

    @BeforeEach
    void setUp() {
        gameMap = createTestMap();

        Player player = new Player(3, 3, 100);

        visibilityMap = new VisibilityMap(7, 7);
        fieldOfView = new FieldOfView(
            gameMap,
            visibilityMap,
            player
        );
    }

    private GameMap createTestMap() {
        int width = 7;
        int height = 7;

        GameMap gameMap = new GameMap(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TilePosition position = new TilePosition(x, y);

                MapCellType cellType;

                if (x == 0 || y == 0
                    || x == width - 1 || y == height - 1) {
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
    void playerTileShouldBeVisibleAfterUpdate() {
        fieldOfView.update();

        TilePosition playerPosition = new TilePosition(3, 3);

        assertTrue(visibilityMap.isCellVisible(playerPosition));
        assertTrue(visibilityMap.isCellExplored(playerPosition));
    }

    @Test
    void openAreaWithinVisibilityRadiusShouldBeVisible() {
        fieldOfView.update();

        assertTrue(
            visibilityMap.isCellVisible(new TilePosition(4, 3)),
            "Adjacent floor tile should be visible"
        );

        assertTrue(
            visibilityMap.isCellVisible(new TilePosition(5, 3)),
            "Floor tile two steps away should be visible"
        );

        assertTrue(
            visibilityMap.isCellVisible(new TilePosition(3, 5)),
            "Floor tile two steps north should be visible"
        );
    }

    @Test
    void wallShouldBeVisibleButTileBehindItShouldNotBeVisible() {
        TilePosition wallPosition = new TilePosition(4, 3);
        TilePosition tileBehindWall = new TilePosition(5, 3);

        gameMap.setCell(
            wallPosition,
            new Cell(wallPosition, MapCellType.WALL)
        );

        fieldOfView.update();

        assertTrue(
            visibilityMap.isCellVisible(wallPosition),
            "Wall should be visible"
        );

        assertFalse(
            visibilityMap.isCellVisible(tileBehindWall),
            "Tile behind the wall should not be visible"
        );
    }

    @Test
    void previouslyExploredTileShouldRemainExploredWhenNoLongerVisible() {
        TilePosition exploredPosition = new TilePosition(2, 3);

        // First update: the tile is visible and becomes explored.
        fieldOfView.update();

        assertTrue(
            visibilityMap.isCellVisible(exploredPosition),
            "Tile should initially be visible"
        );
        assertTrue(
            visibilityMap.isCellExplored(exploredPosition),
            "Visible tile should become explored"
        );

        // Simulate leaving the tile's area of visibility.
        visibilityMap.clearVisible();

        assertFalse(
            visibilityMap.isCellVisible(exploredPosition),
            "Tile should no longer be visible after clearing visibility"
        );
        assertTrue(
            visibilityMap.isCellExplored(exploredPosition),
            "Tile should remain explored after visibility is cleared"
        );
    }
}
