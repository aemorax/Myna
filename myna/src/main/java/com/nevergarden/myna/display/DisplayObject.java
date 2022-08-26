package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.interfaces.IDrawable;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayObject extends EventDispatcher implements IDrawable {
    protected Matrix4f transform;

    protected Vector3f pivot;
    protected Quaternionf rotation;
    protected Vector3f scale;
    protected Vector3f position;

    protected DisplayObjectContainer parent;

    public DisplayObject() {
        this.pivot = new Vector3f(0,0,0);
        this.rotation = new Quaternionf();
        this.scale = new Vector3f(1,1,1);
        this.position = new Vector3f(0,0,0);
        this.transform = new Matrix4f();
        this.transform.identity();
    }

    protected void recalculateMatrix() {
        this.transform.identity();
        if(this.parent != null)
            this.parent.transform.mul(this.transform, this.transform);

        this.transform.translate(this.position, this.transform);
        this.transform.rotate(this.rotation, this.transform);
        this.transform.scale(this.scale, this.transform);
        this.transform.translate(-this.pivot.x, -this.pivot.y, -this.pivot.z, this.transform);
    }

    public void removeFromParent() {
        if(parent != null)
            parent.removeChild(this);
    }

    public void setParent(DisplayObjectContainer parent) {
        if(this.parent != null)
        this.parent.removeEventListeners(Event.TRANSFORM_CHANGE);
        this.parent = parent;
        this.parent.addEventListener(Event.TRANSFORM_CHANGE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                recalculateMatrix();
            }
        });
    }

    public void draw(int frame) {
        throw new AbstractMethodError();
    }

    public void setLocalX(float x) {
        this.position.x = x;
        this.recalculateMatrix();
    }
    public void setLocalY(float y) {
        this.position.y = y;
        this.recalculateMatrix();
    }
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.recalculateMatrix();
    }

    public void setPivotX(float x) {
        this.pivot.x = x;
        this.recalculateMatrix();
    }
    public void setPivotY(float y) {
        this.pivot.y = y;
        this.recalculateMatrix();
    }
    public void setPivot(float x, float y) {
        this.pivot.x = x;
        this.pivot.y = y;
        this.recalculateMatrix();
    }

    public void setRotation(float radians) {
        this.rotation.rotationZ(radians);
        this.recalculateMatrix();
    }
    public void rotate(float radians) {
        this.rotation.rotateLocalZ(radians);
        this.recalculateMatrix();
    }

    public void setScaleX(float x) {
        this.scale.x = x;
        this.recalculateMatrix();
    }
    public void setScaleY(float y) {
        this.scale.y = y;
        this.recalculateMatrix();
    }
    public void setScale(float x, float y) {
        this.scale.x = x;
        this.scale.y = y;
        this.recalculateMatrix();
    }

    public com.nevergarden.myna.math.Vector3f getPosition() {
        return new com.nevergarden.myna.math.Vector3f(this.position.x, this.position.y, 0);
    }
}
