package com.nevergarden.myna.events;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
// import java.util.Stack;

public class EventDispatcher {
    protected Map<String, ArrayList<EventListener>> eventListeners = new HashMap<>();
    // final private Stack<String> _eventStack = new Stack<>();

    public EventDispatcher() {

    }

    public void addEventListener(String type, EventListener listener) {
        ArrayList<EventListener> eventListeners = this.eventListeners.get(type);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            this.eventListeners.put(type, eventListeners);
        }
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    public void removeEventListener(String type, EventListener listener) {
        if (this.eventListeners.containsKey(type)) {
            Objects.requireNonNull(this.eventListeners.get(type)).remove(listener);
            if(Objects.requireNonNull(this.eventListeners.get(type)).isEmpty())
                this.eventListeners.remove(type);
        }
    }

    public void removeEventListeners(String type) {
        this.eventListeners.remove(type);
    }

    public Boolean hasEventListener(String type, EventListener listener) {
        if (!this.eventListeners.containsKey(type))
            return false;
        return Objects.requireNonNull(this.eventListeners.get(type)).contains(listener);
    }

    public void invokeEvent(@NonNull IEvent event) {
        if (!this.eventListeners.containsKey(event.getType())) {
            return;
        }

        ArrayList<EventListener> listeners = this.eventListeners.get(event.getType());
        assert listeners != null;

        if (!listeners.isEmpty()) {
            event.setCurrentTarget(this);
            // _eventStack.push(event.getType());

            int i = 0, listenersSize = listeners.size();
            while (i < listenersSize) {
                EventListener listener = listeners.get(i);
                listener.onEvent(event);
                if (event.getStopsImmediatePropagation()) {
                    // _eventStack.pop();
                    return;
                }
                i++;
            }
        }
    }

    public void dispatchEvent(@NonNull IEvent event) {
        EventDispatcher previousTarget = event.getTarget();
        event.setTarget(this);
        invokeEvent(event);
        if (previousTarget != null) event.setTarget(previousTarget);
    }

    public void dispatchEventWith(String name) {
        dispatchEventWith(name, false, null);
    }

    public void dispatchEventWith(String type, Boolean bubbles, Object data) {
        IEvent event = Event.fromPool(type, bubbles, data);
        dispatchEvent(event);
        Event.toPool(event);
    }
}