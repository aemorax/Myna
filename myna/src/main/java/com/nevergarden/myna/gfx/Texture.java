package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

public class Texture {
    public final int id;
    public final int handle;
    public final int width;
    public final int height;

    public Texture(int id, int handle, int width, int height) {
        this.id = id;
        this.handle = handle;
        this.width = width;
        this.height = height;
    }

    public void bind() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.handle);
    }

    public void unbind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
