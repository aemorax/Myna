package com.nevergarden.myna.events;

import java.util.ArrayList;

public class Event implements IEvent {
    public final static String CONTEXT_CREATE = "context_create";
    public static final String ON_DRAW_FRAME = "on_draw_frame";
    public static final String RESIZE = "resize";
    public static final String TRANSFORM_CHANGE = "transform_change";
    public static final String TOUCH = "touch";
    public static final String LOAD = "load";

    protected final static ArrayList<IEvent> sEventPool = new ArrayList<>();
    protected String type;
    protected Boolean bubbles;
    protected Object data;
    public EventDispatcher target;
    public EventDispatcher currentTarget;
    public Boolean stopsPropagation = false;
    public Boolean stopsImmediatePropagation = false;

    protected Event(String type, Boolean bubbles, Object data) {
        this.type = type;
        this.bubbles = bubbles;
        this.data = data;
    }

    public static IEvent fromPool(String type) {
        return fromPool(type, false, null);
    }

    public static IEvent fromPool(String type, Boolean bubbles) {
        return fromPool(type, bubbles, null);
    }

    public static IEvent fromPool(String type, Boolean bubbles, Object data) {
        if (!sEventPool.isEmpty()) {
            return Event.sEventPool.remove(0).reset(type, bubbles, data);
        }
        return new Event(type, bubbles, data);
    }

    public static void toPool(IEvent event) {
        Event e = (Event) event;
        e.data = e.target = e.currentTarget = null;
        sEventPool.add(e);
    }

    public IEvent reset(String type, Boolean bubbles, Object data) {
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

    @Override
    public EventDispatcher getTarget() {
        return this.target;
    }

    @Override
    public EventDispatcher getCurrentTarget() {
        return this.currentTarget;
    }

    @Override
    public void setTarget(EventDispatcher target) {
        this.target = target;
    }

    @Override
    public void setCurrentTarget(EventDispatcher target) {
        this.currentTarget = target;
    }

    @Override
    public Boolean getStopsImmediatePropagation() {
        return this.stopsImmediatePropagation;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}