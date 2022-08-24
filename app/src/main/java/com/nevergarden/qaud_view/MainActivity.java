package com.nevergarden.qaud_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.ResizeEventData;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.gfx.Quad;
import com.nevergarden.myna.gfx.Sprite;
import com.nevergarden.myna.gfx.Texture;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDispatcher d = new SimpleDispatcher();

        Myna myna = findViewById(R.id.myna);
        myna.eventDispatcher.addEventListener(Event.RESIZE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                ResizeEventData data = (ResizeEventData) event.getData();
            }
        });

        Random r = new Random();


        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(v -> {
            Quad q = new Quad(Color.random(), 200, 200);
            q.setPosition(r.nextInt(myna.getWidth()-200), r.nextInt(myna.getHeight()-200));
            myna.currentStage.addChild(q);
        });

        final float[] scaleX = {1};
        final float[] scaleY = {1};
        final float[] addM = {0.1f};

        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    Quad q = (Quad) myna.currentStage.getChildAt(0);
                    Touch to = t.get(0);
                    if(to!=null) {
                        Log.d(Myna.TAG, to.toString());
                        q.setPosition(to.getX(), to.getY());
                        q.rotate(0.1f);
                        myna.currentStage.setRequiresRedraw(true);
                    }
                }
            }
        });

        myna.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {
                Quad q = new Quad(new Color(100, 200, 0, 255), 200, 200);
                q.setPivot(100, 100);
                q.setPosition(200, 200);

                Texture t = myna.assetManager.loadTexture(R.drawable.pngegg);
                Sprite s = new Sprite(t, new Color(255, 255, 255, 255));
                s.setPivot(150, 150);
                myna.currentStage.addChild(s);
            }
        });
    }
}

class SimpleDispatcher extends EventDispatcher {
    SimpleDispatcher() {
        super();
        this.dispatchEvent(Event.fromPool("create"));
    }
}