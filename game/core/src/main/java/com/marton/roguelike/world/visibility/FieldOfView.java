package com.marton.roguelike.world.visibility;

import com.marton.roguelike.entity.Player;
import com.marton.roguelike.world.GameMap;
import com.marton.roguelike.world.TilePosition;


/**
 * Calculates which map cells are visible from the player's current position.
 * Uses symmetric shadowcasting to account for walls that block vision.
 * Stores current visibility and explored cells in the VisibilityMap.
 */
public class FieldOfView {

    private final GameMap gameMap;
    private final VisibilityMap visibilityMap;
    private final Player player;
    // Maximum visibility distance, measured in map tiles.
    private static final int VISIBILITY_RADIUS = 8;

    public FieldOfView(GameMap gameMap, VisibilityMap visibilityMap, Player player) {
        this.gameMap = gameMap;
        this.visibilityMap = visibilityMap;
        this.player = player;
    }

    public void update() {
        TilePosition playerPosition = getPlayerTilePosition();

        visibilityMap.clearVisible();
        revealCell(playerPosition);

        for (Quadrant quadrant : Quadrant.values()) {
            computeQuadrant(playerPosition, quadrant);
        }
    }

    /**
     * Converts the player's world coordinates to map tile coordinates.
     * Flooring ensures that the player is assigned to the tile containing
     * the player's current world position.
     */
    private TilePosition getPlayerTilePosition() {
        return new TilePosition(
            (int) Math.floor(player.getX()),
            (int) Math.floor(player.getY())
        );
    }

    //Checks whether a target tile is within the circular visibility radius.
    private boolean isWithinRange(
        TilePosition playerPosition,
        TilePosition targetPosition) {

        int dx = targetPosition.x() - playerPosition.x();
        int dy = targetPosition.y() - playerPosition.y();

        int distanceSquared = dx * dx + dy * dy;
        int radiusSquared = VISIBILITY_RADIUS * VISIBILITY_RADIUS;

        return distanceSquared <= radiusSquared;
    }

    /**
     * Starts scanning the first row in the specified quadrant.
     * The initial slope range covers the full 90-degree quadrant.
     */
    private void computeQuadrant(TilePosition playerPosition, Quadrant quadrant) {
        Row firstRow = new Row(1, -1.0, 1.0);
        scanRow(playerPosition, quadrant, firstRow);
    }


    private void scanRow(TilePosition playerPosition, Quadrant quadrant, Row row) {

        // Stop when this row is beyond the visibility radius.
        if (row.depth() > VISIBILITY_RADIUS) {
            return;
        }

        int minColumn = roundTiesUp(row.depth() * row.startSlope());
        int maxColumn = roundTiesDown(row.depth() * row.endSlope());

        Boolean previousTileWasWall = null;

        for (int column = minColumn; column <= maxColumn; column++) {
            TilePosition currentPosition = quadrant.toTilePosition(playerPosition, row.depth(), column);

            if (!gameMap.isInsideMap(currentPosition)) {
                previousTileWasWall = null;
                continue;
            }

            boolean currentTileIsWall = blocksVision(currentPosition);

            if (isWithinRange(playerPosition, currentPosition)
                && (currentTileIsWall || isSymmetric(row, column))) {
                revealCell(currentPosition);
            }

            if (Boolean.FALSE.equals(previousTileWasWall)
                && currentTileIsWall) {

                Row nextRow = new Row(
                    row.depth() + 1,
                    row.startSlope(),
                    slope(row.depth(), column)
                );

                scanRow(playerPosition, quadrant, nextRow);
            }

            if (Boolean.TRUE.equals(previousTileWasWall)
                && !currentTileIsWall) {

                row.startSlope(slope(row.depth(), column));
            }

            previousTileWasWall = currentTileIsWall;
        }

        if (Boolean.FALSE.equals(previousTileWasWall)) {
            Row nextRow = new Row(
                row.depth() + 1,
                row.startSlope(),
                row.endSlope()
            );

            scanRow(playerPosition, quadrant, nextRow);
        }
    }


    // Checks whether a tile's center is within the row's slope boundaries.
    private boolean isSymmetric(Row row, int column) {
        return column >= row.depth() * row.startSlope()
            && column <= row.depth() * row.endSlope();
    }


    // Returns whether the specified tile blocks vision
    private boolean blocksVision(TilePosition position) {
        return gameMap.isInsideMap(position)
            && gameMap.getCell(position).getCellType().blocksVision();
    }

    // Marks a tile as currently visible and permanently explored.
    private void revealCell(TilePosition position) {
        visibilityMap.setCellVisible(position, true);
        visibilityMap.setCellToExplored(position);
    }

    private int roundTiesUp(double value) {
        return (int) Math.floor(value + 0.5);
    }

    private int roundTiesDown(double value) {
        return (int) Math.ceil(value - 0.5);
    }

    private double slope(int depth, int column) {
        return (2.0 * column - 1) / (2.0 * depth);
    }

    private static class Row {

        private final int depth;
        private double startSlope;
        private double endSlope;

        private Row(int depth, double startSlope, double endSlope) {
            this.depth = depth;
            this.startSlope = startSlope;
            this.endSlope = endSlope;
        }

        private int depth() {
            return depth;
        }

        private double startSlope() {
            return startSlope;
        }

        private double endSlope() {
            return endSlope;
        }

        private void startSlope(double startSlope) {
            this.startSlope = startSlope;
        }
    }
}
