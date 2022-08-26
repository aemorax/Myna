package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.math.Vector3f;

import org.joml.Matrix4f;

public class View extends EventDispatcher {
    protected Matrix4f transform;
    protected Vector3f position;

    public View() {
        this.position = new Vector3f();
        this.transform = new Matrix4f();
        this.transform.identity();
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        recalculateMatrix();
    }

    protected void recalculateMatrix() {
        this.transform.identity();
        this.transform.translate(this.position.x, this.position.y, 0);
        IEvent event = Event.fromPool(Event.TRANSFORM_CHANGE);
        this.dispatchEvent(event);
    }
}
