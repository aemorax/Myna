package com.nevergarden.myna.gfx;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.core.MynaThread;
import com.nevergarden.myna.events.Event;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {
    private final Myna myna;
    private final MynaThread thread;
    public Renderer(Myna myna) {
        this.myna = myna;
        this.thread = new MynaThread(myna);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES10.glClearColor(0,1,0,1);
        this.myna.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
        this.thread.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES10.glViewport(0,0, width, height);
        this.myna.eventDispatcher.dispatchEventWith(Event.RESIZE);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);
        this.myna.render(gl10);
    }
}