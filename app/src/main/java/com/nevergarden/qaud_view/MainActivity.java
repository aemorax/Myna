package com.nevergarden.qaud_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDispatcher d = new SimpleDispatcher();

        d.addEventListener("create", new EventListener() {
            @Override
            public void onEvent(Event event) {
                Log.d("Dispatcher", "Hello World");
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