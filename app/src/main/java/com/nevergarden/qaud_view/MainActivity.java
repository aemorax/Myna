package com.nevergarden.qaud_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.display.DisplayObject;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.ResizeEventData;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
            }
        });

        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    myna.mainScene.removeChildAt(0);
                }
            }
        });

        myna.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {
                DisplayObject d = new DisplayObject();
                d.setXYZ(100, 50, 0);
                DisplayObject m = new DisplayObject();
                m.setXYZ(600, 0, 0);

                DisplayObject c = new DisplayObject();
                c.setXYZ(1000, 500, 0);
                d.addChild(c);

                myna.mainScene.addChild(d);
                myna.mainScene.addChild(m);
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