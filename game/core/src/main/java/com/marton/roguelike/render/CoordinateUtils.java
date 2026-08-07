package com.marton.roguelike.render;

public final class CoordinateUtils {
    private CoordinateUtils() {}

    public static float toRenderY(float gameY, int mapHeight) {
        return mapHeight - 1 - gameY;
    }

    public static int toRenderY(int gameY, int mapHeight) {
        return mapHeight - 1 - gameY;
    }
}
