package com.nevergarden.myna.core;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;

import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

public class Myna extends GLSurfaceView {
    public final static String TAG = "Myna";
    public final EventDispatcher eventDispatcher;
    private MynaConfig config;

    public Myna(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.eventDispatcher = new EventDispatcher();
        this.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {
            @Override
            public void onEvent(Event event) {
                Log.d(TAG, "Myna Created");
            }
        });
        this.eventDispatcher.addEventListener(Event.FOCUS, new EventListener() {
            @Override
            public void onEvent(Event event) {
                Log.d(TAG, "Window Focused");
            }
        });

        this.setRenderer(new com.nevergarden.myna.gfx.Renderer(this));
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        this.eventDispatcher.dispatchEventWith(Event.CONTEXT_CREATE);
    }

    public void step() {
        Log.d(TAG, "Step");
    }

    public void render(GL10 gl10) {
        Random r = new Random();
        float red = r.nextFloat();
        float green = r.nextFloat();
        float blue = r.nextFloat();
        gl10.glClearColor(red, green, blue, 1);
        this.requestRender();
    }

    public void setConfig(String configData) {
        this.config = MynaConfigBuilder.create(configData);
    }
}