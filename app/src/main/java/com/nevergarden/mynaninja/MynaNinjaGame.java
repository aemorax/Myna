package com.nevergarden.mynaninja;

import android.content.Context;
import android.util.AttributeSet;

import com.nevergarden.myna.R;
import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.display.DisplayObject;
import com.nevergarden.myna.ds.texturepacker.AsyncTPAtlas;
import com.nevergarden.myna.ds.texturepacker.TPAtlas;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.Touch;
import com.nevergarden.myna.events.TouchEvent;
import com.nevergarden.myna.events.TouchPhase;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.gfx.Quad;
import com.nevergarden.myna.gfx.Sprite;
import com.nevergarden.myna.gfx.TPSpriteAnimation;
import com.nevergarden.myna.gfx.Texture;
import com.nevergarden.myna.math.Vector3f;

import java.util.Map;
import java.util.Random;

public class MynaNinjaGame extends Myna {
    final float[] scaleFactor = {1};

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
                    if(to.getPhase() == TouchPhase.BEGAN) {
                        AsyncTPAtlas asyncTPAtlas = assetManager.loadTexturePackerJsonAtlasAsync(R.drawable.ninja_myna, R.raw.ninja_myna);
                        asyncTPAtlas.addEventListener(Event.LOAD, new EventListener() {
                            @Override
                            public void onEvent(IEvent event) {
                                TPAtlas atlas = asyncTPAtlas.getTPAtlas();
                                TPSpriteAnimation animation = new TPSpriteAnimation(atlas, Color.WHITE, 32, 32);
                                animation.setPivot(16,16);
                                animation.setScale(scaleFactor[0], scaleFactor[0]);
                                animation.setPosition((float) r.nextInt(getWidth()), (float) r.nextInt(getHeight()));
                                currentStage.addChild(animation);
                            }
                        });

                    }
                }
            }
        });

        this.eventDispatcher.addEventListener(Event.CONTEXT_CREATE, new EventListener() {

            @Override
            public void onEvent(IEvent event) {

                currentStage.setColor(new Color(53,51,65,255));
                scaleFactor[0] = getWidth()/180f;

                Texture cloudTex = assetManager.loadTexture(R.drawable.clouds);
                Sprite clouds = new Sprite(cloudTex, Color.WHITE);
                clouds.setPivot((float) cloudTex.width/2, (float) cloudTex.height/2);
                clouds.setPosition((float) (getWidth()/2), (float) (getHeight()/2));
                clouds.setScale(scaleFactor[0], scaleFactor[0]);
                currentStage.addChild(clouds); // 0

                Texture bg2Tex = assetManager.loadTexture(R.drawable.bg_layer2);
                Sprite bg2 = new Sprite(bg2Tex, Color.WHITE);
                bg2.setPivot((float) bg2Tex.width/2, (float) bg2Tex.height);
                bg2.setPosition((float) (getWidth()/2), (float) getHeight()-(scaleFactor[0]*20));
                bg2.setScale(scaleFactor[0], scaleFactor[0]);
                currentStage.addChild(bg2); // 1

                Texture bg1Tex = assetManager.loadTexture(R.drawable.bg_layer1);
                Sprite bg1 = new Sprite(bg1Tex, Color.WHITE);
                bg1.setPivot((float) bg1Tex.width/2, (float) bg1Tex.height);
                bg1.setPosition((float) (getWidth()/2), (float) getHeight()-(scaleFactor[0]*14));
                bg1.setScale(scaleFactor[0], scaleFactor[0]);
                currentStage.addChild(bg1); // 2

                Texture dirtTex = assetManager.loadTexture(R.drawable.dirt);
                Sprite dirt = new Sprite(dirtTex, Color.WHITE);
                dirt.setPivot((float) 0, (float) dirtTex.height);
                dirt.setPosition((float) 0, (float) (getHeight()));
                dirt.setScale(scaleFactor[0], scaleFactor[0]);
                currentStage.addChild(dirt); // 3

                Sprite dirt2 = new Sprite(dirtTex, Color.WHITE);
                dirt2.setPivot((float) 0, (float) dirtTex.height);
                dirt2.setPosition((float) getWidth(), (float) getHeight());
                dirt2.setScale(scaleFactor[0], scaleFactor[0]);
                currentStage.addChild(dirt2); // 4

                Quad quad = new Quad(new Color(48,45,69,110), getWidth(), getHeight());
                quad.setPivot((float) getWidth()/2, (float) getHeight()/2);
                currentStage.addChild(quad); // 5

                TPAtlas ninjaMynaAtlas = assetManager.loadTexturePackerJsonAtlas(R.drawable.ninja_myna, R.raw.ninja_myna);
                TPSpriteAnimation ninjaMyna = new TPSpriteAnimation(ninjaMynaAtlas, Color.WHITE, 32, 32, 6);
                ninjaMyna.setPivot(16,16);
                ninjaMyna.setScale(scaleFactor[0], scaleFactor[0]);
                ninjaMyna.setPosition((float) (getWidth()/2)-(20*getWidth()/100f), (float) (getHeight()/2));
                currentStage.addChild(ninjaMyna); // 6
            }
        });
    }

    @Override
    public void update(long deltaTime) {
        super.update(deltaTime);
        if(deltaTime == 0)
            return;

        DisplayObject myna = currentStage.getChildAt(6);
        Vector3f mynaPos = myna.getPosition();
        mynaPos.y += 0.2f*deltaTime;
        if(mynaPos.y > (getHeight() - (scaleFactor[0]*(42)))) {
            mynaPos.y = (float) getHeight()/2;
        }
        myna.setPosition(mynaPos.x, mynaPos.y);

        DisplayObject d1 = currentStage.getChildAt(3);
        DisplayObject d2 = currentStage.getChildAt(4);
        Vector3f d1Position = d1.getPosition();
        Vector3f d2Position = d2.getPosition();
        d1Position.x -= deltaTime;
        d2Position.x -= deltaTime;
        if(d1Position.x <= -getWidth()) {
            d1Position.x = 0;
            d2Position.x = getWidth();
        }
        d1.setPosition(d1Position.x, d1Position.y);
        d2.setPosition(d2Position.x, d2Position.y);
    }
}
