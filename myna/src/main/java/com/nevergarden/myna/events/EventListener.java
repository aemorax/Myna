package com.nevergarden.myna.events;

/**
 * Default event listeners interface
 */
public interface EventListener extends java.util.EventListener {
    default void onEvent(IEvent event) {

    }
}