package com.nevergarden.myna.ds.texturepacker;

import androidx.annotation.NonNull;

public class TPFrame {
    public String filename;
    public TPRectInfo frame;
    public Boolean rotated;
    public Boolean trimmed;
    public TPRectInfo spriteSourceSize;
    public TPSizeInfo sourceSize;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "filename='" + filename + '\'' +
                ", frame=" + frame +
                ", rotated=" + rotated +
                ", trimmed=" + trimmed +
                ", spriteSourceSize=" + spriteSourceSize +
                ", sourceSize=" + sourceSize +
                '}';
    }
}
