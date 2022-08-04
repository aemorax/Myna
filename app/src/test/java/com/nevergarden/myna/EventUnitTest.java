package com.nevergarden.myna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.nevergarden.myna.events.Event;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EventUnitTest {
    @Test
    public void type_isCorrect() {
        Event event = Event.fromPool("create", false, null);
        assertEquals(event.getType(), "create");
        Event.toPool(event);
    }

    @Test
    public void data_isCorrect() {
        Event event = Event.fromPool("create", false, new TestObjectWithName());
        assertEquals(((TestObjectWithName) event.getData()).name, "Test");
        Event.toPool(event);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void event_poolingWorks() {
        Event event = Event.fromPool("event");
        Event event2 = Event.fromPool("event2");
        Event.toPool(event);
        Event.toPool(event2);
        try {
            Class<? extends Event> eventClass = event.getClass();
            Field sEventPool = eventClass.getDeclaredField("sEventPool");
            sEventPool.setAccessible(true);
            ArrayList<Event> mEventPool = (ArrayList<Event>) sEventPool.get(event);

            assert mEventPool != null;
            assertEquals(mEventPool.size(), 2);

            Event newEvent = Event.fromPool("new", false, null);
            assertEquals(mEventPool.size(), 1);

            assertEquals(event, newEvent);
            assertNotEquals(event, Event.fromPool("another"));
        } catch (NoSuchFieldException e) {
            Assert.fail("Field: sEventPool does not exist in Event");
        } catch (IllegalAccessException e) {
            Assert.fail();
        }
    }

    @Test
    public void event_setTypeWorks() {
        Event wrongEventType = Event.fromPool("main");
        assertEquals(wrongEventType.getType(), "main");
        wrongEventType.setType("create");
        assertNotEquals(wrongEventType.getType(), "main");
    }

    @Test
    public void event_setBubblesWorks() {
        Event noBubbleEvent = Event.fromPool("newEvent");
        noBubbleEvent.setBubbles(true);

        assertEquals(noBubbleEvent.getBubbles(), true);

        Event withBubble = Event.fromPool("another", true);
        assertEquals(withBubble.getBubbles(), true);
    }

    @Test
    public void event_setDataWorks() {
        Event noDataEvent = Event.fromPool("newEvent");
        TestObjectWithName someData = new TestObjectWithName();
        noDataEvent.setData(someData);

        assertNotEquals(noDataEvent.getData(), null);
        assertEquals(noDataEvent.getData(), someData);
    }
}

class TestObjectWithName {
    public String name = "Test";
}