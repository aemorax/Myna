package com.nevergarden.myna;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class EventDispatcherUnitTest {
    public static boolean shouldPass;
    @Test
    public void test_eventAddingAndDispatching() {
        EventDispatcher eventDispatcher = new EventDispatcher();

        shouldPass = false;

        eventDispatcher.addEventListener("create", new EventListener() {
            @Override
            public void onEvent(Event event) {
                shouldPass = true;
            }
        });

        eventDispatcher.dispatchEvent(Event.fromPool("create"));
        if(!shouldPass)
            Assert.fail("EventDispatcher.dispatchEvent is not working.");

        shouldPass = false;
        eventDispatcher.dispatchEventWith("create");
        if(!shouldPass)
            Assert.fail("EventDispatcher.dispatchEventWith is not working.");
    }

    @Test
    public void test_hasEventListenerWorks() {
        EventDispatcher eventDispatcher = new EventDispatcher();
        EventListener someEventListener = new EventListener() {
            @Override
            public void onEvent(Event event) {}
        };
        EventListener notAddedEventListener = new EventListener() {
            @Override
            public void onEvent(Event event) {}
        };
        eventDispatcher.addEventListener("create", someEventListener);
        assertEquals(eventDispatcher.hasEventListener("create", someEventListener), true);
        assertEquals(eventDispatcher.hasEventListener("create", notAddedEventListener), false);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_removingEventListeners() throws IllegalAccessException {
        EventDispatcher eventDispatcher = new EventDispatcher();
        EventListener knownListener = new EventListener() {
            @Override
            public void onEvent(Event event) {}
        };

        eventDispatcher.addEventListener("create", knownListener);
        eventDispatcher.addEventListener("remove", knownListener);
        eventDispatcher.addEventListener("remove", new EventListener() {
            @Override
            public void onEvent(Event event) {}
        });

        eventDispatcher.addEventListener("hasTwo", knownListener);
        eventDispatcher.addEventListener("hasTwo", new EventListener() {
            @Override
            public void onEvent(Event event) {}
        });

        Class<? extends EventDispatcher> eventDispatcherC = eventDispatcher.getClass();
        try {
            Field dispatcherListeners = eventDispatcherC.getDeclaredField("eventListeners");
            dispatcherListeners.setAccessible(true);

            Map<String, ArrayList<EventListener>> eventListeners = (Map<String, ArrayList<EventListener>>) dispatcherListeners.get(eventDispatcher);
            assert eventListeners != null;
            assertTrue(eventListeners.containsKey("create"));
            assertTrue(eventListeners.containsKey("remove"));

            // removeListener
            eventDispatcher.removeEventListener("create", knownListener);
            assertFalse(eventListeners.containsKey("create"));

            // removeListeners
            eventDispatcher.removeEventListeners("remove");
            assertFalse(eventListeners.containsKey("remove"));

            // removeOneListener should not empty a eventDispatcher
            eventDispatcher.removeEventListener("hasTwo", knownListener);
            assertTrue(eventListeners.containsKey("hasTwo"));
        } catch (NoSuchFieldException e) {
            Assert.fail("EventDispatcher.eventListeners not found");
        }
    }
}