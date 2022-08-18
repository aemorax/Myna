package com.nevergarden.qaud_view;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quad q = new Quad(Color.random(), 200, 200);
                q.x = r.nextInt(myna.getWidth()-200);
                q.y = r.nextInt(myna.getHeight()-200);
                Log.d("Sample", ""+q.x + ":" + q.y);
                q.recalculateMatrix();
                myna.currentStage.addChild(q);
            }
        });

        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    myna.currentStage.removeChildAt(0);
                    myna.currentStage.setColor(Color.random());
                }
            }
        });

        myna.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {
                Quad q = new Quad(new Color(100, 200, 0, 255), 200, 200);
                q.x = 200;
                q.y = 200;
                myna.currentStage.addChild(q);
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