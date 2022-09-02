package com.nevergarden.myna.events;

/**
 * Event interface.
 */
public interface IEvent {
    /**
     * Get type of current event.
     */
    String getType();

    /**
     * Sets type of current event.
     */
    void setType(String type);

    /**
     * Get bubbles property.
     */
    Boolean getBubbles();

    /**
     * Sets bubbles property.
     */
    void setBubbles(Boolean bubbles);

    /**
     * Get data of current data.
     */
    Object getData();

    /**
     * Sets data of current data.
     */
    void setData(Object data);

    /**
     * Get target.
     */
    EventDispatcher getTarget();

    /**
     * Set target.
     */
    void setTarget(EventDispatcher target);

    EventDispatcher getCurrentTarget();
    void setCurrentTarget(EventDispatcher target);

    Boolean getStopsImmediatePropagation();

    /**
     * Resets the event.
     */
    IEvent reset(String type, Boolean bubbles, Object data);
}