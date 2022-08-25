package com.nevergarden.myna.ds.texturepacker;

import androidx.annotation.NonNull;

public class TPRectInfo {
    public Integer x;
    public Integer y;
    public Integer w;
    public Integer h;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}
