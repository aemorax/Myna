package com.nevergarden.myna.core;

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

import com.nevergarden.myna.display.Stage;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.ds.Color;
import com.nevergarden.myna.util.AssetManager;

import java.util.Map;

/**
 * Core engine view, which should be extended to be used.
 */
public class Myna extends GLSurfaceView {
    /**
     * Default TAG used for game engine logging.
     */
    public final static String TAG = "Myna";

    /**
     * AssetManager for this view.
     *
     * @see AssetManager
     */
    public final AssetManager assetManager;

    /**
     * Most of view and renderer events are dispatched using eventDispatcher here.
     */
    public final EventDispatcher eventDispatcher;

    /**
     * Main renderer of this view.
     *
     * @see com.nevergarden.myna.gfx.Renderer
     */
    public final com.nevergarden.myna.gfx.Renderer renderer;

    public final MynaThread thread;

    private final MynaConfig config;

    /**
     * Current view Stage, can be changed using `setCurrentStage`.
     */
    protected Stage currentStage;

    /**
     * Myna base `View` class, should be extended to be able to make it work on updates.
     * This class handles inputs, updates and loops.
     *
     * @param context Application Context
     * @param attrs   Attributes
     */
    public Myna(Context context, AttributeSet attrs) {
        super(context, attrs);
        // AssetManager initialization.
        this.assetManager = new AssetManager(this);

        // EventDispatcher initialization.
        this.eventDispatcher = new EventDispatcher();

        // Initialize config before creating renderer.
        this.config = new MynaConfig();
        this.setConfig(attrs);

        // Initialize renderer.
        this.thread = new MynaThread(this);
        this.renderer = new com.nevergarden.myna.gfx.Renderer(this);
        this.setRenderer(this.renderer);
        this.setRenderMode(this.config.getRenderMode());
    }

    /**
     * A method to override click.
     *
     * @return boolean if clickListener is was called
     */
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Handles all motion events for eventDispatcher override if eventDispatcher is not wanted.
     *
     * @param event MotionEvent
     * @return boolean true if event is handled false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Map<Integer, Touch> touches = Touch.byNativeEvent(event);
        eventDispatcher.dispatchEvent(TouchEvent.fromPool(touches, false));
        this.performClick();
        return super.onTouchEvent(event);
    }

    /**
     * Changes the current stage to another stage.
     * If default stage is not wanted don't call super method of `Myna.init()`.
     *
     * @param newStage a newly created stage
     * @param dispose  dispose the previous stage
     */
    public void setCurrentStage(Stage newStage, Boolean dispose) {
        Stage s = setCurrentStage(newStage);
        if (dispose) {
            s.dispose();
        }
    }

    /**
     * Changes current stage and returns the previous stage.
     *
     * @param newStage a newly created stage
     * @return Stage previous stage
     */
    public Stage setCurrentStage(Stage newStage) {
        Stage previousStage = this.currentStage;
        this.currentStage = newStage;
        this.currentStage.setSize(this.renderer.getWidth(), this.renderer.getHeight());
        return previousStage;
    }

    /**
     * Override this method to populate assets on load.
     */
    public void loadAssets() {
    }

    /**
     * Override this method to populate stage.
     */
    public void init() {
        this.createDefaultStage();
    }

    /**
     * Override this method for update calls.
     * This method is called before rendering.
     *
     * @param deltaTime time passed since last render
     */
    public void update(long deltaTime) {
        if (deltaTime == 0)
            return;
        if (currentStage.getRequiresRedraw()) {
            currentStage.addAll();
            currentStage.setRequiresRedraw(false);
        }
    }

    /**
     * Override this method if you want custom resizing logic.
     *
     * @param width  new width
     * @param height new height
     */
    public void onResize(int width, int height) {
    }

    /**
     * Called on each render and draws all stage drawables.
     * Don't override unless you know what you are doing.
     */
    public void render() {
        Color c = this.currentStage.getColor();
        GLES10.glClearColor(c.getFRed(), c.getFGreen(), c.getFBlue(), c.getFAlpha());
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);
        this.eventDispatcher.dispatchEventWith(Event.RENDER);
        currentStage.drawAll();
        GLES10.glFinish();
    }

    private void createDefaultStage() {
        Stage defaultStage = new Stage(this, new Color(0.0f, 0.0f, 0.0f, 1.0f));
        this.setCurrentStage(defaultStage, false);
    }

    private void setConfig(AttributeSet configData) {
        TypedArray typedArray = getContext().obtainStyledAttributes(configData, new int[]{
                com.nevergarden.myna.R.attr.myna_pixelFormat,
                com.nevergarden.myna.R.attr.myna_renderMode});
        int id = typedArray.getInteger(0, 0);
        MynaPixelFormat pixelFormat = MynaPixelFormat.fromId(id);
        this.config.setPixelFormat(pixelFormat);

        id = typedArray.getInteger(1, 0);
        MynaRenderMode renderMode = MynaRenderMode.fromId(id);
        this.config.setRenderMode(renderMode);

        typedArray.recycle();

        // Find the best version of OpenGL ES
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        int eglVersion = configurationInfo.reqGlEsVersion;
        if (eglVersion >= 0x30000) {
            this.setEGLContextClientVersion(3);
        } else if (eglVersion >= 0x20000) {
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
            case OPAQUE:
                holder.setFormat(PixelFormat.OPAQUE);
                break;
            case RGBX_8888:
                holder.setFormat(PixelFormat.RGBX_8888);
                break;
            case RGBA_8888:
                holder.setFormat(PixelFormat.RGBA_8888);
                break;
            case RGB_565:
                holder.setFormat(PixelFormat.RGB_565);
                break;
            case RGB_888:
                holder.setFormat(PixelFormat.RGB_888);
                break;
            case TRANSPARENT:
                holder.setFormat(PixelFormat.TRANSPARENT);
                break;
            case UNKNOWN:
                holder.setFormat(PixelFormat.UNKNOWN);
                break;
        }
    }
}