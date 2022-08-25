package com.nevergarden.myna.ds.texturepacker;

import androidx.annotation.NonNull;

public class TPSizeInfo {
    public Integer w;
    public Integer h;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "w=" + w +
                ", h=" + h +
                '}';
    }
}
