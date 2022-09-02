package com.nevergarden.myna.gfx;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.ResizeEventData;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Default Myna Renderer.
 */
public class Renderer implements GLSurfaceView.Renderer {
    private final Myna myna;

    private int width = 0;
    private int height = 0;

    private int counter = 0;

    public Renderer(Myna myna) {
        this.myna = myna;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.myna.loadAssets();
        this.myna.init();
        this.myna.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
        this.myna.thread.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES10.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
        this.myna.onResize(width, height);
        this.myna.eventDispatcher.dispatchEventWith(Event.RESIZE, false, new ResizeEventData(width, height));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        this.myna.render();
        this.counter++;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getCounter() {
        return counter;
    }
}