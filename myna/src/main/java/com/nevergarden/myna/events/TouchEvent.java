package com.nevergarden.myna.events;

import java.util.ArrayList;

public class TouchEvent extends Event implements IEvent {
    private static final ArrayList<IEvent> sEventPool = new ArrayList<>();
    private TouchEvent(String type) {
        this(type, null);
    }

    private TouchEvent(String type, Touch[] touches) {
        this(type, touches, false);
    }

    private TouchEvent(String type, Touch[] touches, Boolean bubbles) {
        super(type, bubbles, touches);
    }


    public static IEvent fromPool(Touch[] touches, Boolean bubbles) {
        if(!TouchEvent.sEventPool.isEmpty()) {
            return TouchEvent.sEventPool.remove(0).reset("touch", bubbles, touches);
        }
        return new TouchEvent("touch",touches, bubbles);
    }

    public Touch[] getData() {
        return (Touch[]) this.data;
    }
}
