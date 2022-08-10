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
import com.nevergarden.myna.gfx.Quad;
import com.nevergarden.myna.gfx.Triangle;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static Triangle triangle;
    private static Quad quad;

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
                triangle = new Triangle(
                        new float[]{0.1f, 0.1f, 0.5f, 1.0f},
                        new float[]{
                                0, 0.4f, 0.0f, // top
                                -0.3f, -0.3f, 0.0f, // bottom right
                                0.3f, -0.3f, 0.0f
                        });

                quad = new Quad(
                        new float[]{0.1f, 0.1f, 0.5f, 1.0f},
                        new float[]{
                                0.3f, 0.3f, 0.0f, // top right
                                -0.3f, 0.3f, 0.0f, // top left
                                -0.3f, -0.3f, 0.0f, // bottom left
                                -0.3f, -0.3f, 0.0f, // bottom left
                                0.3f, 0.3f, 0.0f, // top right
                                0.3f, -0.3f, 0.0f
                        }
                );
            }
        });

        myna.eventDispatcher.addEventListener(Event.ON_DRAW_FRAME, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                /*float[] newColor = new float[4];
                Random rand = new Random();
                for (int i = 0; i < 3; i++) {
                    newColor[i] = rand.nextFloat();
                }
                newColor[3] = 1;
                triangle.changeColor(newColor);
                triangle.draw();
                 */
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