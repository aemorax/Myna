package com.nevergarden.myna.display;

import android.opengl.Matrix;
import android.util.Log;

import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.interfaces.IDrawable;

import org.joml.Matrix4f;

import java.util.Arrays;

public class DisplayObject extends EventDispatcher implements IDrawable {
    public float x, y, pivotX, pivotY, scaleX, scaleY, skewX, skewY, rotation, alpha;
    private boolean visible;

    protected Matrix4f mainMatrix;
    protected Matrix4f localMatrix;
    protected DisplayObjectContainer parent;

    public DisplayObject() {
        x = y = pivotY = pivotX = rotation = skewX = skewY = 0.0f;
        scaleX = scaleY = alpha = 1.0f;
        visible = true;
        this.localMatrix = new Matrix4f();
        this.mainMatrix = new Matrix4f();
        this.localMatrix.identity();
        this.mainMatrix.identity();
        recalculateMatrix();
    }

    protected void recalculateMatrix() {
        this.localMatrix.identity();
        if(this.parent != null) {
            float[] m = new float[16];
            this.localMatrix.set(3, 0, x);
            this.localMatrix.set(3, 1, y);
            this.localMatrix.get(m);
            Matrix4f x = new Matrix4f();
            parent.localMatrix.get(x);
            this.mainMatrix = x.mul(this.localMatrix);
        }
        else {
            this.mainMatrix = this.localMatrix;
        }
    }

    public void removeFromParent() {
        if(parent != null)
            parent.removeChild(this);
    }

    public void setParent(DisplayObjectContainer parent) {
        this.parent = parent;
    }

    public void draw() {
        throw new AbstractMethodError();
    }
}
