package com.nevergarden.myna.ds.texturepacker;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class TPAtlasInfo {
    public TPFrame[] frames;
    public TPMeta meta;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "frames=" + Arrays.toString(frames) +
                ", meta=" + meta +
                '}';
    }
}

