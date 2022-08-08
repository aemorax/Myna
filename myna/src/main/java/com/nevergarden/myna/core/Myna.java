package com.nevergarden.myna.core;

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

import javax.microedition.khronos.opengles.GL10;

public class Myna extends GLSurfaceView {
    public final static String TAG = "Myna";

    public final EventDispatcher eventDispatcher;
    private final com.nevergarden.myna.gfx.Renderer renderer;
    private final MynaConfig config = new MynaConfig();

    public Myna(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.eventDispatcher = new EventDispatcher();
        //this.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, event -> Log.d(TAG, "Myna Created"));
        //this.eventDispatcher.addEventListener(Event.FOCUS, event -> Log.d(TAG, "Window Focused"));

        this.setConfig(attrs);
        this.renderer = new com.nevergarden.myna.gfx.Renderer(this);
        this.setRenderer(this.renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //this.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        synchronized (this.renderer.thread) {
            Touch[] touches = Touch.byNativeEvent(event);
            eventDispatcher.dispatchEvent(TouchEvent.fromPool(touches, false));
            // event.recycle();
        }
        return true;
    }

    public void step() {}

    public void render(GL10 gl10) {
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