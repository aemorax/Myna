package com.nevergarden.myna.util;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.gfx.Texture;

public class AsyncTexture extends EventDispatcher implements Runnable {
    public final int id;
    public final AssetManager assetManager;
    private Texture texture;

    public AsyncTexture(AssetManager assetManager, int id) {
        this.assetManager = assetManager;
        this.id = id;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void run() {
        this.texture = assetManager.loadTexture(id);
        this.dispatchEventWith(Event.LOAD);
    }
}
