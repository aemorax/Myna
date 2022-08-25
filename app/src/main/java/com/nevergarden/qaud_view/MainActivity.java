package com.nevergarden.qaud_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.ds.texturepacker.TPAtlas;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.gfx.Quad;
import com.nevergarden.myna.gfx.Sprite;
import com.nevergarden.myna.gfx.TPSpriteAnimation;
import com.nevergarden.myna.gfx.Texture;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Myna myna = findViewById(R.id.myna);

        Random r = new Random();
        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(v -> {
            Sprite s = new Sprite(myna.assetManager.loadTexture(R.drawable.pngegg), new Color(1f,1f,1f,1f));
            s.setPosition(r.nextInt(myna.getWidth()-200), r.nextInt(myna.getHeight()-200));
            myna.currentStage.addChild(s);
        });

        myna.eventDispatcher.addEventListener("touch", new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    Quad q = (Quad) myna.currentStage.getChildAt(0);
                    Touch to = t.get(0);
                    if(to!=null) {
                        q.setPosition(to.getX(), to.getY());
                        myna.currentStage.setRequiresRedraw(true);
                    }
                }
            }
        });

        myna.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {

                myna.currentStage.setColor(new Color(64,90,115,255));

                TPAtlas tpAtlas = myna.assetManager.loadTexturePackerJsonAtlas(R.drawable.bouncing_glass, R.raw.bouncing_glass);
                TPSpriteAnimation spriteAnimation = new TPSpriteAnimation(tpAtlas, new Color(1f,1f,1f,1f), 32, 32);
                spriteAnimation.setPivot(16,16);
                spriteAnimation.setScale(20, 20);
                myna.currentStage.addChild(spriteAnimation);

                Texture t = myna.assetManager.loadTexture(R.drawable.pngegg);
                Sprite s = new Sprite(t, new Color(255, 255, 255, 255));
                s.setPivot(150, 150);
                myna.currentStage.addChild(s);
            }
        });
    }
}