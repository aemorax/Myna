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
    private final Myna myna;
    private final MynaThread mynaThread;

    private int width = 0;
    private int height = 0;

    private int counter = 0;

    public Renderer(Myna myna) {
        this.myna = myna;
        this.mynaThread = new MynaThread(myna);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Stage newStage = new Stage(this.myna, new Color(0.0f,0.0f,0.0f,1.0f));
        this.myna.setCurrentStage(newStage, false);
        this.myna.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
        this.mynaThread.start();
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