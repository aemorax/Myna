package com.nevergarden.myna.gfx;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.core.MynaThread;
import com.nevergarden.myna.display.Stage;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.ResizeEventData;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {
    public final MynaThread thread;
    private final Myna myna;

    private int width = 0;
    private int height = 0;

    public Renderer(Myna myna) {
        this.myna = myna;
        this.thread = new MynaThread(myna);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.thread.start();
        Stage newStage = new Stage(this.myna, new Color(0.0f,0.0f,0.0f,1.0f));
        this.myna.setCurrentStage(newStage, false);
        this.myna.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES10.glViewport(0,0, width, height);
        this.width = width;
        this.height = height;
        this.myna.eventDispatcher.dispatchEventWith(Event.RESIZE, false, new ResizeEventData(width, height));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Color c = this.myna.currentStage.getColor();
        GLES10.glClearColor(c.getFRed(), c.getFGreen(), c.getFBlue(), c.getFAlpha());
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);
        this.myna.eventDispatcher.dispatchEventWith(Event.ON_DRAW_FRAME);
        this.myna.render();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}