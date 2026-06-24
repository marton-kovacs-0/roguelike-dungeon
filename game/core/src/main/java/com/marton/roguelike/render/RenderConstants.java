package com.marton.roguelike.render;

public final class RenderConstants {
    public static final int SOURCE_TILE_SIZE = 16;
    public static final int RENDER_SCALE = 3;
    public static final int RENDER_TILE_SIZE =
        SOURCE_TILE_SIZE * RENDER_SCALE;

    private RenderConstants() {}
}
