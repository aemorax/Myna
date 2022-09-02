package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

/**
 * Texture Class
 */
public class Texture {
    /**
     * ID for this texture, usually resource id.
     */
    public final int id;
    /**
     * Texture width.
     */
    public final int width;
    /**
     * Texture height.
     */
    public final int height;

    /**
     * Native texture handle.
     */
    private final int handle;

    public Texture(int id, int handle, int width, int height) {
        this.id = id;
        this.handle = handle;
        this.width = width;
        this.height = height;
    }

    /**
     * Binds the texture for use.
     */
    public void bind() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.handle);
    }

    /**
     * Unbinds the texture.
     */
    public void unbind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
