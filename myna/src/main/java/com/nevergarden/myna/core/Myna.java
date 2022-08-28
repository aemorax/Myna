package com.nevergarden.myna.core;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.nevergarden.myna.R;
import com.nevergarden.myna.display.Stage;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.util.AssetManager;

import java.util.Map;

public class Myna extends GLSurfaceView {
    public final static String TAG = "Myna";

    public Stage currentStage;
    public final AssetManager assetManager;

    public final EventDispatcher eventDispatcher;
    public final com.nevergarden.myna.gfx.Renderer renderer;
    private final MynaConfig config = new MynaConfig();

    public Myna(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.assetManager = new AssetManager(this);
        this.eventDispatcher = new EventDispatcher();
        this.setConfig(attrs);
        this.renderer = new com.nevergarden.myna.gfx.Renderer(this);
        this.setRenderer(this.renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Map<Integer, Touch> touches = Touch.byNativeEvent(event);
        eventDispatcher.dispatchEvent(TouchEvent.fromPool(touches, false));
        performClick();
        return true;
    }

    public void setCurrentStage(Stage newStage, Boolean dispose) {
        Stage s = setCurrentStage(newStage);
        if(dispose) {
            s.dispose();
        }
    }

    public Stage setCurrentStage(Stage newStage) {
        Stage previousStage = this.currentStage;
        this.currentStage = newStage;
        this.currentStage.setSize(this.renderer.getWidth(), this.renderer.getHeight());
        return previousStage;
    }

    public void update(long deltaTime) {
        if(deltaTime == 0)
            return;
        if(currentStage.getRequiresRedraw()) {
            currentStage.addAll();
            currentStage.setRequiresRedraw(false);
        }
    }

    public void render() {
        Color c = this.currentStage.getColor();
        GLES10.glClearColor(c.getFRed(), c.getFGreen(), c.getFBlue(), c.getFAlpha());
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);
        this.eventDispatcher.dispatchEventWith(Event.ON_DRAW_FRAME);
        currentStage.drawAll();
        GLES10.glFinish();
    }

    public void setConfig(AttributeSet configData) {
        TypedArray typedArray = getContext().obtainStyledAttributes(configData, new int[]{R.attr.myna_pixelFormat});
        int id = typedArray.getInteger(0, 0);
        typedArray.recycle();
        MynaPixelFormat pixelFormat = MynaPixelFormat.fromId(id);
        this.config.setPixelFormat(pixelFormat);

        // Find the best version of OpenGL ES
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        int eglVersion = configurationInfo.reqGlEsVersion;
        if(eglVersion >= 0x30000) {
            this.setEGLContextClientVersion(3);
        } else if(eglVersion >= 0x20000) {
            this.setEGLContextClientVersion(2);
        } else {
            throw new Error("Not Supported EGL version");
        }

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
    }
}