package com.nevergarden.qaud_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.display.DisplayObjectContainer;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayObjectContainer a = new DisplayObjectContainer();
        DisplayObjectContainer m = new DisplayObjectContainer();
        a.addChild(m);

        SimpleDispatcher d = new SimpleDispatcher();

        d.addEventListener("create", new EventListener() {
            @Override
            public void onEvent(Event event) {
                a.addChild(m);
                Log.d("Myna", "count:" + a.getChildrenCount());
            }
        });

        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(view -> d.dispatchEvent(Event.fromPool("create")));
    }
}

class SimpleDispatcher extends EventDispatcher {
    SimpleDispatcher() {
        super();
        this.dispatchEvent(Event.fromPool("create"));
    }
}