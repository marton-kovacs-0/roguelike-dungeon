package com.marton.roguelike.world.visibility;

import com.marton.roguelike.world.TilePosition;

public class VisibilityMap {

    private final boolean[][] visible;
    private final boolean[][] explored;

    public VisibilityMap(int width, int height) {
        this.visible = new boolean[width][height];
        this.explored = new boolean[width][height];
    }

    public boolean isCellVisible(TilePosition position) {
        return visible[position.x()][position.y()];
    }

    public boolean isCellExplored(TilePosition position) {
        return explored[position.x()][position.y()];
    }

    public void setCellToExplored(TilePosition position) {
        explored[position.x()][position.y()] = true;
    }

    public void setCellVisible(TilePosition position, boolean isVisible) {
        visible[position.x()][position.y()] = isVisible;
    }

    public void clearVisible() {
        for (int x = 0; x < visible.length; x++) {
            for (int y = 0; y < visible[x].length; y++) {
                visible[x][y] = false;
            }
        }
    }

}
