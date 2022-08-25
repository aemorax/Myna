package com.nevergarden.myna.ds.texturepacker;

import com.nevergarden.myna.gfx.Texture;

public class TPAtlas {
    public final Texture texture;
    public final TPAtlasInfo atlasInfo;

    public TPAtlas(Texture texture, TPAtlasInfo info) {
        this.texture = texture;
        this.atlasInfo = info;
    }
}
