
package com.marton.roguelike.world.visibility;

import com.marton.roguelike.world.TilePosition;

/**
 * Represents one of the four cardinal quadrants used by shadowcasting.
 * Stores the coordinate directions for moving forward (depth)
 * and sideways (column) within each quadrant.
 */
public enum Quadrant {
    NORTH(0, 1, 1, 0),
    EAST(1, 0, 0, 1),
    SOUTH(0, -1, 1, 0),
    WEST(-1, 0, 0, 1);

    private final int depthX;
    private final int depthY;
    private final int columnX;
    private final int columnY;

    Quadrant(int depthX, int depthY, int columnX, int columnY) {
        this.depthX = depthX;
        this.depthY = depthY;
        this.columnX = columnX;
        this.columnY = columnY;
    }

    public TilePosition toTilePosition(
        TilePosition origin, int depth, int column) {

        int x = origin.x() + depth * depthX + column * columnX;
        int y = origin.y() + depth * depthY + column * columnY;

        return new TilePosition(x, y);
    }
}
