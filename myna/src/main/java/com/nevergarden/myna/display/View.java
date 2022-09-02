package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.ds.Vector3f;

import org.joml.Matrix4f;

/**
 * A View is the same concept as a camera, a viewer or anything that moves
 * and can view the world.
 */
public class View extends EventDispatcher {
    /** Transformation of this camera. */
    protected Matrix4f transform;
    /** Current position of view. */
    protected Vector3f position;

    /**
     * Constructor.
     */
    public View() {
        this.position = new Vector3f();
        this.transform = new Matrix4f();
        this.transform.identity();
    }

    /**
     * Get position.
     * @return position
     */
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * Set current position of the view.
     * @param x x value
     * @param y y value
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        recalculateMatrix();
    }

    /**
     * Recalculates the view matrix used by stage.
     */
    protected void recalculateMatrix() {
        this.transform.identity();
        this.transform.translate(this.position.x, this.position.y, 0);
        IEvent event = Event.fromPool(Event.TRANSFORM_CHANGE);
        this.dispatchEvent(event);
    }
}
