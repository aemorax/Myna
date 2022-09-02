package com.nevergarden.myna.core;

import android.opengl.GLSurfaceView;

enum MynaPixelFormat {
    OPAQUE(0),
    RGBX_8888(1),
    RGBA_8888(2),
    RGB_565(3),
    RGB_888(4),
    TRANSPARENT(5),
    UNKNOWN(6);

    int id;

    MynaPixelFormat(int id) {
        this.id = id;
    }

    static MynaPixelFormat fromId(int id) {
        for (MynaPixelFormat f : values()) {
            if (f.id == id) return f;
        }
        throw new IllegalArgumentException();
    }
}

enum MynaRenderMode {
    INVALIDATION(0),
    CONTINUOUSLY(1);

    int id;

    MynaRenderMode(int id) {
        this.id = id;
    }

    static MynaRenderMode fromId(int id) {
        for (MynaRenderMode m : values()) {
            if (m.id == id) return m;
        }
        throw new IllegalArgumentException();
    }
}

class MynaConfig {
    private int redSize = 8;
    private int greenSize = 8;
    private int blueSize = 8;
    private int alphaSize = 8;

    private MynaPixelFormat pixelFormat;
    private MynaRenderMode renderMode;

    public MynaConfig() {
    }

    public MynaPixelFormat getPixelFormat() {
        return pixelFormat;
    }

    public void setPixelFormat(MynaPixelFormat pixelFormat) {
        this.pixelFormat = pixelFormat;
        switch (this.pixelFormat) {
            case OPAQUE:
            case RGB_888:
            case UNKNOWN:
                this.redSize = this.greenSize = this.blueSize = 8;
                this.alphaSize = 0;
                break;
            case RGB_565:
                this.redSize = this.blueSize = 5;
                this.greenSize = 6;
                this.alphaSize = 0;
                break;
            case RGBA_8888:
            case RGBX_8888:
            case TRANSPARENT:
                this.redSize = this.greenSize = this.blueSize = this.alphaSize = 8;
                break;
        }
    }

    public int getRenderMode() {
        switch (renderMode) {
            case INVALIDATION:
                return GLSurfaceView.RENDERMODE_WHEN_DIRTY;
            case CONTINUOUSLY:
                return GLSurfaceView.RENDERMODE_CONTINUOUSLY;
        }
        return GLSurfaceView.RENDERMODE_WHEN_DIRTY;
    }

    public void setRenderMode(MynaRenderMode renderMode) {
        this.renderMode = renderMode;
    }

    public int getRedSize() {
        return redSize;
    }

    public int getGreenSize() {
        return greenSize;
    }

    public int getBlueSize() {
        return blueSize;
    }

    public int getAlphaSize() {
        return alphaSize;
    }

    public int getDepthSize() {
        return 0;
    }
}
