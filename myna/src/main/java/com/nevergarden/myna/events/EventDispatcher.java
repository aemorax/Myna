package com.nevergarden.myna.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for dispatching events
 */
public class EventDispatcher {
    /**
     * Map of type to event listeners.
     */
    protected Map<String, ArrayList<EventListener>> eventListeners = new HashMap<>();

    public EventDispatcher() {
    }

    /**
     * Adds a new event listener of type to dispatcher.
     */
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

    /**
     * Removes an event listener of type.
     */
    public void removeEventListener(String type, EventListener listener) {
        if (this.eventListeners.containsKey(type)) {
            Objects.requireNonNull(this.eventListeners.get(type)).remove(listener);
            if (Objects.requireNonNull(this.eventListeners.get(type)).isEmpty())
                this.eventListeners.remove(type);
        }
    }

    /**
     * Removes all event listeners of type.
     */
    public void removeEventListeners(String type) {
        this.eventListeners.remove(type);
    }

    /**
     * Checks if has an event listener.
     */
    public Boolean hasEventListener(String type, EventListener listener) {
        if (!this.eventListeners.containsKey(type))
            return false;
        return Objects.requireNonNull(this.eventListeners.get(type)).contains(listener);
    }

    /**
     * Invokes the event
     */
    public void invokeEvent(IEvent event) {
        if (event != null && !this.eventListeners.containsKey(event.getType())) {
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

    /**
     * Sends a new event.
     */
    public void dispatchEvent(IEvent event) {
        if (event == null)
            return;
        EventDispatcher previousTarget = event.getTarget();
        event.setTarget(this);
        invokeEvent(event);
        if (previousTarget != null) event.setTarget(previousTarget);
    }

    /**
     * Sends a new event with type only.
     */
    public void dispatchEventWith(String name) {
        dispatchEventWith(name, false, null);
    }

    /**
     * Sends a new event with values.
     */
    public void dispatchEventWith(String type, Boolean bubbles, Object data) {
        IEvent event = Event.fromPool(type, bubbles, data);
        dispatchEvent(event);
        Event.toPool(event);
    }

    /**
     * Disposes the event dispatcher.
     */
    public void dispose() {
        this.eventListeners.clear();
    }
}