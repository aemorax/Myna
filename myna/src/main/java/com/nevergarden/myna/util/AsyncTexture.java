package com.nevergarden.myna.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.gfx.Texture;

public class AsyncTexture extends EventDispatcher implements Runnable {
    public final int id;
    public final AssetManager assetManager;
    private Resources resources;
    private Texture texture;

    public AsyncTexture(AssetManager assetManager, int id, Resources resources) {
        this.assetManager = assetManager;
        this.id = id;
        this.resources = resources;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void run() {
        this.texture = assetManager.getTexture(id);
        if(texture != null) {
            this.dispatchEventWith(Event.LOAD);
            return;
        }
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(this.resources, this.id, opt);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        this.texture = new Texture(id, textureHandle[0], bitmap.getWidth(), bitmap.getHeight());
        assetManager.putTexture(id, texture);
        bitmap.recycle();
        this.dispatchEventWith(Event.LOAD);
    }
}
