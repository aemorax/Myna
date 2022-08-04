package com.nevergarden.myna.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class EventDispatcher {
    final private Map<String, ArrayList<EventListener>> _eventListeners = new HashMap<>();
    final private Stack<String> _eventStack = new Stack<>();

    public EventDispatcher() {

    }

    public void addEventListener(String type, EventListener listener) {
        ArrayList<EventListener> eventListeners = _eventListeners.get(type);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            this._eventListeners.put(type, eventListeners);
        }
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    public void removeEventListener(String type, EventListener listener) {
        if (this._eventListeners.containsKey(type)) {
            Objects.requireNonNull(this._eventListeners.get(type)).remove(listener);
        }
    }

    public void removeEventListeners(String type) {
        this._eventListeners.remove(type);
    }

    public Boolean hasEventListener(String type, EventListener listener) {
        if (!this._eventListeners.containsKey(type))
            return false;
        return Objects.requireNonNull(this._eventListeners.get(type)).contains(listener);
    }

    public Boolean invokeEvent(Event event) {
        if (!this._eventListeners.containsKey(event.getType())) {
            return false;
        }

        ArrayList<EventListener> listeners = this._eventListeners.get(event.getType());

        if (!listeners.isEmpty()) {
            event.currentTarget = this;
            _eventStack.push(event.getType());


            Iterator<EventListener> listenerIterator = listeners.iterator();
            while (listenerIterator.hasNext()) {
                EventListener listener = listenerIterator.next();
                listener.onEvent(event);
                if (event.stopsImmediatePropagation) {
                    _eventStack.pop();
                    return false;
                }
            }
            return event.stopsPropagation;
        }
        return false;
    }

    public void dispatchEvent(Event event) {
        EventDispatcher previousTarget = event.target;
        event.target = this;
        invokeEvent(event);
        if (previousTarget != null) event.target = previousTarget;
    }

    public void dispatchEventWith(String name) {
        dispatchEventWith(name, false, null);
    }

    public void dispatchEventWith(String name, Boolean bubbles) {
        dispatchEventWith(name, bubbles, null);
    }

    public void dispatchEventWith(String type, Boolean bubbles, Object data) {
        Event event = Event.fromPool(type, bubbles, data);
        dispatchEvent(event);
        Event.toPool(event);
    }
}