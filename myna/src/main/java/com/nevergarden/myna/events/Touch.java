package com.nevergarden.myna.events;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Touch {
    private final static ArrayList<Touch> sEventPool = new ArrayList<>();
    private final static Map<Integer, Touch> touches = new HashMap<>();
    private int touchID;
    private float x;
    private float y;
    private TouchPhase phase;
    private float pressure;
    private long downTime;

    private Touch(int id) {
        touchID = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Touch{" +
                "touchID=" + touchID +
                ", x=" + x +
                ", y=" + y +
                ", phase=" + phase +
                ", pressure=" + pressure +
                ", downTime=" + downTime +
                '}';
    }
    public static Touch[] byNativeEvent(MotionEvent event) {
        Touch[] touchList = new Touch[event.getPointerCount()];

        int index = event.getActionIndex();
        Log.d("TD", "0:" + event.getX(0) + ":" + event.getPointerId(0));
        if(event.getPointerCount() > 1)
            Log.d("TD", "1:" + event.getX(1) + ":" + event.getPointerId(1));
        return touchList;
    }

    public static Touch fromPool(int id) {
        if(sEventPool.isEmpty())
            return new Touch(id);
        return sEventPool.remove(0).reset(id);
    }

    public static void toPool(Touch touch) {
        sEventPool.add(touch);
    }

    public void dispose() {
        Touch.toPool(this);
    }

    private Touch reset(int id) {
        this.touchID = id;
        this.x = 0;
        this.y = 0;
        this.pressure = 0;
        this.downTime = 0;
        this.phase = TouchPhase.ENDED;
        return this;
    }
}