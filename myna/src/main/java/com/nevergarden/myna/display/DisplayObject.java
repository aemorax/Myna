package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Base of every object that is rendered into screen.
 * <br/><br/>
 * <b>Display Transformations</b><br/>
 * The most important part of <code>DisplayObject</code> job is to take care of transformation matrix
 * calculations.
 *
 * @see Quad
 * @see Sprite
 */
public abstract class DisplayObject extends EventDispatcher implements Drawable {
    protected Matrix4f transform;

    protected Vector3f pivot;
    protected Quaternionf rotation;
    protected Vector3f scale;
    protected Vector3f position;

    /**
     * Parent of current node.
     *
     * Most of the nodes are added to stage.
     */
    protected DisplayObjectContainer parent;

    /**
     * Default Constructor.
     */
    public DisplayObject() {
        this.pivot = new Vector3f(0, 0, 0);
        this.rotation = new Quaternionf();
        this.scale = new Vector3f(1, 1, 1);
        this.position = new Vector3f(0, 0, 0);
        this.transform = new Matrix4f();
        this.transform.identity();
    }

    /**
     * Recalculates the matrix with it's current transformations.
     */
    protected void recalculateMatrix() {
        this.transform.identity();
        if (this.parent != null)
            this.parent.transform.mul(this.transform, this.transform);

        this.transform.translate(this.position, this.transform);
        this.transform.rotate(this.rotation, this.transform);
        this.transform.scale(this.scale, this.transform);
        this.transform.translate(-this.pivot.x, -this.pivot.y, -this.pivot.z, this.transform);
        if (this.parent != null)
            this.parent.recalculateMatrix();
    }

    /**
     * Removes self from parent.
     */
    public void removeFromParent() {
        if (parent != null)
            parent.removeChild(this);
    }

    /**
     * Sets a new parent to this DisplayObject
     * @param parent New Parent
     */
    public void setParent(DisplayObjectContainer parent) {
        if (this.parent != null)
            this.parent.removeEventListeners(Event.TRANSFORM_CHANGE);
        this.parent = parent;
        this.recalculateMatrix();
    }

    public void draw(int frame) {
        throw new AbstractMethodError();
    }

    /**
     * Sets current DisplayObject X position relative to it's parent.
     */
    public void setLocalX(float x) {
        this.position.x = x;
        this.recalculateMatrix();
    }

    /**
     * Sets current DisplayObject Y position relative to it's parent.
     */
    public void setLocalY(float y) {
        this.position.y = y;
        this.recalculateMatrix();
    }

    /**
     * Sets position of current DisplayObject relative to it's parent.
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.recalculateMatrix();
    }

    /**
     * Sets X pivot position of current DisplayObject
     */
    public void setPivotX(float x) {
        this.pivot.x = x;
        this.recalculateMatrix();
    }

    /**
     * Sets Y pivot position of current DisplayObject
     */
    public void setPivotY(float y) {
        this.pivot.y = y;
        this.recalculateMatrix();
    }

    /**
     * Sets Piviot point of current DisplayObject
     */
    public void setPivot(float x, float y) {
        this.pivot.x = x;
        this.pivot.y = y;
        this.recalculateMatrix();
    }

    /**
     * Sets current object rotation in radians.
     */
    public void setRotation(float radians) {
        this.rotation.rotationZ(radians);
        this.recalculateMatrix();
    }

    /**
     * Rotates current object in radians.
     */
    public void rotate(float radians) {
        this.rotation.rotateLocalZ(radians);
        this.recalculateMatrix();
    }

    /**
     * Sets current object X scale.
     */
    public void setScaleX(float x) {
        this.scale.x = x;
        this.recalculateMatrix();
    }

    /**
     * Sets current object Y scale.
     */
    public void setScaleY(float y) {
        this.scale.y = y;
        this.recalculateMatrix();
    }

    /**
     * Sets current object scale.
     */
    public void setScale(float x, float y) {
        this.scale.x = x;
        this.scale.y = y;
        this.recalculateMatrix();
    }

    /**
     * Get position of current object.
     */
    public com.nevergarden.myna.ds.Vector3f getPosition() {
        return new com.nevergarden.myna.ds.Vector3f(this.position.x, this.position.y, 0);
    }
}
