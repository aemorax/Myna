package com.nevergarden.myna.events;

import java.util.ArrayList;

public class Event {
    public static final String FOCUS = "focus";
    public static final String CONTEXT_CREATE = "contextCreate";
    public static final String RESIZE = "resize";

    private final static ArrayList<Event> sEventPool = new ArrayList<>();
    private String type;
    private Boolean bubbles;
    private Object data;
    public EventDispatcher target;
    public EventDispatcher currentTarget;
    public Boolean stopsPropagation = false;
    public Boolean stopsImmediatePropagation = false;

    private Event(String type, Boolean bubbles, Object data) {
        this.type = type;
        this.bubbles = bubbles;
        this.data = data;
    }

    public static Event fromPool(String type) {
        return fromPool(type, false, null);
    }

    public static Event fromPool(String type, Boolean bubbles) {
        return fromPool(type, bubbles, null);
    }

    public static Event fromPool(String type, Boolean bubbles, Object data) {
        if (!sEventPool.isEmpty()) {
            return sEventPool.remove(0).reset(type, bubbles, data);
        }
        return new Event(type, bubbles, data);
    }

    public static void toPool(Event event) {
        event.data = event.target = event.currentTarget = null;
        sEventPool.add(event);
    }

    private Event reset(String type, Boolean bubbles, Object data) {
        this.type = type;
        this.bubbles = bubbles;
        this.data = data;
        this.target = this.currentTarget = null;
        this.stopsPropagation = this.stopsImmediatePropagation = false;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getBubbles() {
        return bubbles;
    }

    public void setBubbles(Boolean bubbles) {
        this.bubbles = bubbles;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}