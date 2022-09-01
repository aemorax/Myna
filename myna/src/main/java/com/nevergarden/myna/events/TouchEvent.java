package com.nevergarden.myna.events;

import java.util.ArrayList;
import java.util.Map;

public class TouchEvent extends Event implements IEvent {
    private static final ArrayList<IEvent> sEventPool = new ArrayList<>();

    private TouchEvent(String type) {
        this(type, null);
    }

    private TouchEvent(String type, Map<Integer, Touch> touches) {
        this(type, touches, false);
    }

    private TouchEvent(String type, Map<Integer, Touch> touches, Boolean bubbles) {
        super(type, bubbles, touches);
    }


    public static IEvent fromPool(Map<Integer, Touch> touches, Boolean bubbles) {
        if (!TouchEvent.sEventPool.isEmpty()) {
            return TouchEvent.sEventPool.remove(0).reset(Event.TOUCH, bubbles, touches);
        }
        return new TouchEvent("touch", touches, bubbles);
    }

    public Map<Integer, Touch> getData() {
        return (Map<Integer, Touch>) this.data;
    }
}
