package com.nevergarden.myna;

import static org.junit.Assert.assertEquals;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.IEvent;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EventUnitTest {
    @Test
    public void type_isCorrect() {
        IEvent event = Event.fromPool("create", false, null);
        Assert.assertEquals(event.getType(), "create");
        Event.toPool(event);
    }

    @Test
    public void data_isCorrect() {
        IEvent event = Event.fromPool("create", false, new TestObjectWithName());
        assertEquals(((TestObjectWithName) event.getData()).name, "Test");
        Event.toPool(event);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void event_poolingWorks() {
        IEvent event = Event.fromPool("event");
        IEvent event2 = Event.fromPool("event2");
        Event.toPool(event);
        Event.toPool(event2);
        try {
            Class<? extends IEvent> eventClass = event.getClass();
            Field sEventPool = eventClass.getDeclaredField("sEventPool");
            sEventPool.setAccessible(true);
            ArrayList<IEvent> mEventPool = (ArrayList<IEvent>) sEventPool.get(event);

            assert mEventPool != null;
            assertEquals(mEventPool.size(), 2);

            IEvent newEvent = Event.fromPool("new", false, null);
            assertEquals(mEventPool.size(), 1);

            Assert.assertEquals(event, newEvent);
            Assert.assertNotEquals(event, Event.fromPool("another"));
        } catch (NoSuchFieldException e) {
            Assert.fail("Field: sEventPool does not exist in Event");
        } catch (IllegalAccessException e) {
            Assert.fail();
        }
    }

    @Test
    public void event_setTypeWorks() {
        IEvent wrongEventType = Event.fromPool("main");
        Assert.assertEquals(wrongEventType.getType(), "main");
        wrongEventType.setType("create");
        Assert.assertNotEquals(wrongEventType.getType(), "main");
    }

    @Test
    public void event_setBubblesWorks() {
        IEvent noBubbleEvent = Event.fromPool("newEvent");
        noBubbleEvent.setBubbles(true);

        Assert.assertEquals(noBubbleEvent.getBubbles(), true);

        IEvent withBubble = Event.fromPool("another", true);
        Assert.assertEquals(withBubble.getBubbles(), true);
    }

    @Test
    public void event_setDataWorks() {
        IEvent noDataEvent = Event.fromPool("newEvent");
        TestObjectWithName someData = new TestObjectWithName();
        noDataEvent.setData(someData);

        Assert.assertNotEquals(noDataEvent.getData(), null);
        Assert.assertEquals(noDataEvent.getData(), someData);
    }
}

class TestObjectWithName {
    public String name = "Test";
}