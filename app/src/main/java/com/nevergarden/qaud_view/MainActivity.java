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
import com.nevergarden.myna.gfx.Quad;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Quad quad;
    private static int width = 1080;
    private static int height = 1920;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDispatcher d = new SimpleDispatcher();
        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(view -> d.dispatchEvent(Event.fromPool("create")));

        Myna myna = findViewById(R.id.myna);
        myna.eventDispatcher.addEventListener(Event.RESIZE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                ResizeEventData data = (ResizeEventData) event.getData();
                width = data.width;
                height = data.height;
            }
        });

        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    Touch m = t.get(0);
                    assert m != null;
                    Log.d("Myna", "Touch: " + m.getId() + " " + "Delta: " + m.deltaTime() + " Duration: " + m.duration());
                }

                for (Touch x:t.values()) {
                    if(x!=null)
                        x.dispose();
                }
            }
        });

        myna.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {
                Log.d("Myna", "wh" + width + ":" + height);
                quad = new Quad(
                        new float[]{0.1f, 0.1f, 0.5f, 1.0f},
                        new float[]{
                                width, height, 0f, // top right
                                0, height, 0.0f, // top left
                                0, 0, 0.0f, // bottom left
                                0, 0, 0.0f, // bottom left
                                width, height, 0.0f, // top right
                                width, 0, 0.0f // bottom right
                        }
                );
            }
        });

        myna.eventDispatcher.addEventListener(Event.ON_DRAW_FRAME, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                quad.draw();
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