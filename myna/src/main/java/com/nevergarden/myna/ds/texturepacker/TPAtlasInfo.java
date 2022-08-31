package com.nevergarden.myna.ds.texturepacker;

import java.util.Arrays;

public class TPAtlasInfo {
    public TPFrame[] frames;
    public TPMeta meta;

    @Override
    public String toString() {
        return "{" +
                "frames=" + Arrays.toString(frames) +
                ", meta=" + meta +
                '}';
    }
}

