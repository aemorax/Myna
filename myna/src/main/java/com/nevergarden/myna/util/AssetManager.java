package com.nevergarden.myna.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.google.gson.Gson;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.ds.texturepacker.AsyncTPAtlas;
import com.nevergarden.myna.ds.texturepacker.TPAtlas;
import com.nevergarden.myna.ds.texturepacker.TPAtlasInfo;
import com.nevergarden.myna.gfx.Texture;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final Myna myna;
    private final Map<Integer, Texture> textures = new HashMap<>();
    public AssetManager(Myna myna) {
        this.myna = myna;
    }

    public TPAtlas loadTexturePackerJsonAtlas(int textureId, int spriteSheetId) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(myna.getResources(), textureId, opt);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        Texture t = new Texture(textureId, textureHandle[0], bitmap.getWidth(), bitmap.getHeight());
        bitmap.recycle();

        InputStream inputStream = myna.getResources().openRawResource(spriteSheetId);

        Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        Gson gson = new Gson();
        TPAtlasInfo atlasInfo = gson.fromJson(reader, TPAtlasInfo.class);
        return new TPAtlas(t, atlasInfo);
    }

    public AsyncTPAtlas loadTexturePackerJsonAtlasAsync(int id, int atlasID) {
        AsyncTPAtlas asyncTPAtlas = new AsyncTPAtlas(this, id, atlasID);
        this.myna.queueEvent(asyncTPAtlas);
        return asyncTPAtlas;
    }

    public Texture loadTexture(int id) {
        if(textures.containsKey(id))
            return textures.get(id);

        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(myna.getResources(), id, opt);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        Texture t = new Texture(id, textureHandle[0], bitmap.getWidth(), bitmap.getHeight());
        bitmap.recycle();

        this.textures.put(id, t);
        return t;
    }

    public AsyncTexture loadTextureAsync(int id) {
        AsyncTexture texture = new AsyncTexture(this, id);
        this.myna.queueEvent(texture);
        return texture;
    }
}
