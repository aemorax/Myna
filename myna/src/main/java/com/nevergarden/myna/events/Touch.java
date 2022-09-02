package com.nevergarden.myna.events;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A Touch property.
 */
public class Touch {
    private final static ArrayList<Touch> sEventPool = new ArrayList<>();
    private final static Map<Integer, Touch> touches = new HashMap<>();
    private int id;
    private int index;
    private float x;
    private float y;
    private float lastX;
    private float lastY;
    private long lastMoveTime = 0;
    private TouchPhase phase;
    private float pressure;
    private long downTime = 0;

    private Touch(int id) {
        this.id = id;
    }

    /**
     * Generates a touch map from native android MotionEvent.
     */
    public static Map<Integer, Touch> byNativeEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int pointer = event.getPointerId(index);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                Touch t;
                if (touches.containsKey(pointer))
                    t = touches.get(pointer);
                else
                    t = Touch.fromPool(pointer);
                if (t != null) {
                    t.index = index;
                    t.lastX = 0;
                    t.lastY = 0;
                    t.x = event.getX();
                    t.y = event.getY();
                    t.pressure = event.getPressure();
                    t.downTime = event.getDownTime();
                    t.lastMoveTime = event.getEventTime();
                    t.phase = TouchPhase.BEGAN;
                    touches.put(pointer, t);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); i++) {
                    Touch t = null;
                    pointer = event.getPointerId(i);
                    if (touches.containsKey(pointer))
                        t = touches.get(pointer);
                    if (t != null) {
                        t.index = i;
                        t.lastX = t.x;
                        t.lastY = t.y;
                        t.x = event.getX(i);
                        t.y = event.getY(i);
                        t.pressure = event.getPressure(i);
                        t.lastMoveTime = event.getEventTime();
                        t.phase = TouchPhase.MOVED;
                        touches.put(pointer, t);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                Touch t = touches.get(pointer);
                if (t != null) {
                    t.lastX = t.x;
                    t.lastY = t.y;
                    t.x = event.getX();
                    t.y = event.getY();
                    t.lastMoveTime = event.getEventTime();
                    t.pressure = event.getPressure();
                    t.phase = TouchPhase.ENDED;
                    touches.remove(pointer);
                }
                break;
            }
        }

        return touches;
    }

    public static Touch fromPool(int id) {
        if (sEventPool.isEmpty())
            return new Touch(id);
        return sEventPool.remove(0).reset(id);
    }

    public static void toPool(Touch touch) {
        sEventPool.add(touch);
    }

    @Override
    public String toString() {
        return "Touch{" +
                "id=" + id +
                ", index=" + index +
                ", x=" + x +
                ", y=" + y +
                ", dX=" + lastX +
                ", dY=" + lastY +
                ", phase=" + phase +
                ", pressure=" + pressure +
                ", downTime=" + downTime +
                '}';
    }

    public void dispose() {
        Touch.toPool(this);
    }

    private Touch reset(int id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.pressure = 0;
        this.downTime = 0;
        this.phase = TouchPhase.ENDED;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public TouchPhase getPhase() {
        return phase;
    }

    public float getPressure() {
        return pressure;
    }

    public long getDownTime() {
        return downTime;
    }

    public long duration() {
        return this.lastMoveTime - this.downTime;
    }

    public long deltaTime() {
        return android.os.SystemClock.elapsedRealtime() - this.lastMoveTime;
    }
}