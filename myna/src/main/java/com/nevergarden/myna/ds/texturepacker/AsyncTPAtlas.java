package com.nevergarden.myna.ds.texturepacker;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.util.AssetManager;

public class AsyncTPAtlas extends EventDispatcher implements Runnable {
    final AssetManager assetManager;
    final int id;
    final int atlasID;
    private TPAtlas tpAtlas;

    public AsyncTPAtlas(AssetManager assetManager, int id, int atlasID) {
        this.assetManager = assetManager;
        this.id = id;
        this.atlasID = atlasID;
    }

    public TPAtlas getTPAtlas() {
        return tpAtlas;
    }

    @Override
    public void run() {
        this.tpAtlas = this.assetManager.loadTexturePackerJsonAtlas(id, atlasID);
        this.dispatchEventWith(Event.LOAD);
    }
}
