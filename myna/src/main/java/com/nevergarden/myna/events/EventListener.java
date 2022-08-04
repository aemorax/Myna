package com.nevergarden.myna.events;

public interface EventListener extends java.util.EventListener {
    default void onEvent(Event event) {

    }
}
