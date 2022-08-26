package com.nevergarden.mynaninja;

import android.content.Context;
import android.util.AttributeSet;

import com.nevergarden.myna.R;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.display.View;
import com.nevergarden.myna.ds.texturepacker.TPAtlas;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.events.TouchPhase;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.gfx.Sprite;
import com.nevergarden.myna.gfx.TPSpriteAnimation;
import com.nevergarden.myna.math.Vector3f;

import java.util.Map;
import java.util.Random;

public class MynaNinjaGame extends Myna {

    public MynaNinjaGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        Random r = new Random();

        this.eventDispatcher.addEventListener(Event.TOUCH, new EventListener() {
            @Override
            public void onEvent(IEvent event) {

                TouchEvent touchEvent = (TouchEvent) event;
                Map<Integer, Touch> t = touchEvent.getData();

                if(t.containsKey(0)) {
                    Touch to = t.get(0);
                    assert to != null;
                    if(to.getPhase() == TouchPhase.MOVED) {
                        View view = currentStage.getView();
                        Vector3f v = view.getPosition();
                        v.x += to.getX() - to.getLastX();
                        v.y += to.getY() - to.getLastY();
                        view.setPosition(v.x, v.y);
                    }
                }
            }
        });

        this.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {

                currentStage.setColor(new Color(64,90,115,255));

                TPAtlas tpAtlas = assetManager.loadTexturePackerJsonAtlas(R.drawable.bouncing_glass, R.raw.bouncing_glass);
                TPSpriteAnimation spriteAnimation = new TPSpriteAnimation(tpAtlas, new Color(1f,1f,1f,1f), 32, 32, 2);
                spriteAnimation.setPivot(16,16);
                spriteAnimation.setScale(20, 20);
                spriteAnimation.setPosition((int) (getWidth()/2), (int) (getHeight()/2));
                currentStage.addChild(spriteAnimation);

                TPSpriteAnimation spriteAnimation1 = new TPSpriteAnimation(tpAtlas, new Color(1f,1f,1f,1f), 32, 32, 1);
                spriteAnimation1.setPivot(16,16);
                spriteAnimation1.setScale(20, 20);
                spriteAnimation1.setPosition(getWidth(), getHeight());
                currentStage.addChild(spriteAnimation1);

                Sprite s = new Sprite(assetManager.loadTexture(R.drawable.pngegg), new Color(1f,1f,1f,1f));
                s.setPosition(r.nextInt(getWidth()-200), r.nextInt(getHeight()-200));
                currentStage.addChild(s);

                s = new Sprite(assetManager.loadTexture(R.drawable.pngegg), new Color(1f,1f,1f,1f));
                s.setPosition(r.nextInt(getWidth()-200), r.nextInt(getHeight()-200));
                currentStage.addChild(s);
            }
        });
    }

    @Override
    public void update(long deltaTime) {
        super.update(deltaTime);
    }
}
