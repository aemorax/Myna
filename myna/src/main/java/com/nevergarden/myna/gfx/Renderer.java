package com.nevergarden.myna.gfx;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.core.MynaThread;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.ResizeEventData;

import org.joml.Matrix4f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {
    public final static float[] PROJECTION = new float[16];
    private final static Matrix4f PROJECTION_MAT = new Matrix4f();
    public final MynaThread thread;
    private final Myna myna;
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
        Log.d("Myna", "widthheght" + width + ":" + height);
        PROJECTION_MAT.identity();
        PROJECTION_MAT.ortho(0, width, 0, height, -1, 1);
        PROJECTION_MAT.get(PROJECTION);
        this.myna.eventDispatcher.dispatchEventWith(Event.RESIZE, false, new ResizeEventData(width, height));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);
        this.myna.eventDispatcher.dispatchEventWith(Event.ON_DRAW_FRAME);
        this.myna.render();
    }
}