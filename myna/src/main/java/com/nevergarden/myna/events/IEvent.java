package com.nevergarden.myna.events;

public interface IEvent {
    String getType();

    void setType(String type);

    Boolean getBubbles();

    void setBubbles(Boolean bubbles);

    Object getData();

    void setData(Object data);

    EventDispatcher getTarget();

    void setTarget(EventDispatcher target);

    EventDispatcher getCurrentTarget();

    void setCurrentTarget(EventDispatcher target);

    Boolean getStopsImmediatePropagation();

    IEvent reset(String type, Boolean bubbles, Object data);
}