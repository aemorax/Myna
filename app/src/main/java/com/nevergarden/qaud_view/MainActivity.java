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
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDispatcher d = new SimpleDispatcher();
        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(view -> d.dispatchEvent(Event.fromPool("create")));

        Myna myna = findViewById(R.id.myna);
        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Touch[] t = touchEvent.getData();
                for (Touch m:t) {
                    if(m!=null) {
                        Log.d("Myna", m.toString());
                        m.dispose();
                    }
                }
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