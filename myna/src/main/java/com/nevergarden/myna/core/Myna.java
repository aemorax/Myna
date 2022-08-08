package com.nevergarden.myna.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.nevergarden.myna.R;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;

import java.util.Map;

public class Myna extends GLSurfaceView {
    public final static String TAG = "Myna";

    public final EventDispatcher eventDispatcher;
    private final com.nevergarden.myna.gfx.Renderer renderer;
    private final MynaConfig config = new MynaConfig();

    public Myna(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.eventDispatcher = new EventDispatcher();

        this.setConfig(attrs);
        this.renderer = new com.nevergarden.myna.gfx.Renderer(this);
        this.setRenderer(this.renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        synchronized (this.renderer.thread) {
            Map<Integer, Touch> touches = Touch.byNativeEvent(event);
            eventDispatcher.dispatchEvent(TouchEvent.fromPool(touches, false));
            performClick();
            // TODO: write touch handler in a way that can recycle event at the end.
            // event.recycle();
        }
        return true;
    }

    public void step() {}

    public void render() {
        this.requestRender();
    }

    public void setConfig(AttributeSet configData) {
        TypedArray typedArray = getContext().obtainStyledAttributes(configData, new int[]{R.attr.myna_pixelFormat});
        int id = typedArray.getInteger(0, 0);
        typedArray.recycle();
        MynaPixelFormat pixelFormat = MynaPixelFormat.fromId(id);
        this.config.setPixelFormat(pixelFormat);

        setEGLConfigChooser(
                this.config.getRedSize(),
                this.config.getRedSize(),
                this.config.getBlueSize(),
                this.config.getAlphaSize(),
                this.config.getDepthSize(),
                this.config.getAlphaSize()
        );
        SurfaceHolder holder = getHolder();
        switch (this.config.getPixelFormat()) {
            case OPAQUE: holder.setFormat(PixelFormat.OPAQUE); break;
            case RGBX_8888: holder.setFormat(PixelFormat.RGBX_8888); break;
            case RGBA_8888: holder.setFormat(PixelFormat.RGBA_8888); break;
            case RGB_565: holder.setFormat(PixelFormat.RGB_565); break;
            case RGB_888: holder.setFormat(PixelFormat.RGB_888); break;
            case TRANSPARENT: holder.setFormat(PixelFormat.TRANSPARENT); break;
            case UNKNOWN: holder.setFormat(PixelFormat.UNKNOWN); break;
        }

        Log.d(TAG, "r:" + this.config.getRedSize() +
                "g:" + this.config.getGreenSize() +
                "b:" + this.config.getBlueSize() +
                "a:" + this.config.getAlphaSize()
        );
    }
}