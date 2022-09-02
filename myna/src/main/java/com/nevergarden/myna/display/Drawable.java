package com.nevergarden.myna.display;

/**
 * Interface for everything that can be drawn by the renderer.
 *
 * @see com.nevergarden.myna.gfx.Renderer
 */
public interface Drawable {
    /**
     * Draws the drawable on the given frame.
     */
    void draw(int frame);
}
