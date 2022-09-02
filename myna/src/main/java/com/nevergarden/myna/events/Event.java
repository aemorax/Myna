package com.nevergarden.myna.events;

import java.util.ArrayList;

/**
 * Event Data Structure.
 */
public class Event implements IEvent {
    /**
     * Event type for created context.
     */
    public final static String CONTEXT_CREATE = "context_create";
    /**
     * Event type dispatched before drawing.
     */
    public static final String RENDER = "render";
    /**
     * Event type dispatched after resizing context.
     */
    public static final String RESIZE = "resize";
    /**
     * Event type dispatched when a transform changes in tree.
     */
    public static final String TRANSFORM_CHANGE = "transform_change";
    /**
     * Event type dispatched when screen is touched.
     */
    public static final String TOUCH = "touch";
    /**
     * Event type dispatched by loaders when load is complete.
     */
    public static final String LOAD = "load";

    /**
     * Event pool.
     */
    protected final static ArrayList<IEvent> sEventPool = new ArrayList<>();
    /**
     * Target of event dispatcher.
     */
    public EventDispatcher target;
    /**
     * Current target of dispatcher when it bubbles.
     */
    public EventDispatcher currentTarget;
    /**
     * Event type.
     */
    protected String type;
    /**
     * If Event bubbles up through event listeners.
     *
     * @apiNote In progress.
     */
    protected Boolean bubbles;
    /**
     * Data passed by the event.
     */
    protected Object data;

    private Boolean stopsPropagation = false;
    private Boolean stopsImmediatePropagation = false;

    protected Event(String type, Boolean bubbles, Object data) {
        this.type = type;
        this.bubbles = bubbles;
        this.data = data;
    }

    /**
     * Gets a new event from pool with type.
     */
    public static IEvent fromPool(String type) {
        return fromPool(type, false, null);
    }

    /**
     * Gets a new event from pool with type and bubbles.
     */
    public static IEvent fromPool(String type, Boolean bubbles) {
        return fromPool(type, bubbles, null);
    }

    /**
     * Gets a new event from pool with type and bubbles and data.
     */
    public static IEvent fromPool(String type, Boolean bubbles, Object data) {
        if (!sEventPool.isEmpty()) {
            return Event.sEventPool.remove(0).reset(type, bubbles, data);
        }
        return new Event(type, bubbles, data);
    }

    /**
     * Sends an unused event to pool.
     */
    public static void toPool(IEvent event) {
        Event e = (Event) event;
        e.data = e.target = e.currentTarget = null;
        sEventPool.add(e);
    }

    /**
     * Resets the event data.
     */
    public IEvent reset(String type, Boolean bubbles, Object data) {
        this.type = type;
        this.bubbles = bubbles;
        this.data = data;
        this.target = this.currentTarget = null;
        this.stopsPropagation = this.stopsImmediatePropagation = false;
        return this;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Boolean getBubbles() {
        return bubbles;
    }

    @Override
    public void setBubbles(Boolean bubbles) {
        this.bubbles = bubbles;
    }

    @Override
    public EventDispatcher getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(EventDispatcher target) {
        this.target = target;
    }

    @Override
    public EventDispatcher getCurrentTarget() {
        return this.currentTarget;
    }

    @Override
    public void setCurrentTarget(EventDispatcher target) {
        this.currentTarget = target;
    }

    @Override
    public Boolean getStopsImmediatePropagation() {
        return this.stopsImmediatePropagation;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }
}