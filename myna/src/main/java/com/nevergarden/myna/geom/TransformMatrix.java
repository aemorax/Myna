package com.nevergarden.myna.geom;

import org.joml.Math;
import org.joml.Matrix4f;

public class TransformMatrix {
    private final Matrix4f translation = new Matrix4f();
    private final Matrix4f rotation = new Matrix4f();
    private final Matrix4f scale = new Matrix4f();

    public Matrix4f transform = new Matrix4f();

    public TransformMatrix() {
        this.translation.identity();
        this.rotation.identity();
        this.scale.identity();
        this.transform.identity();
    }

    public void setScale(float x, float y, float z) {
        scale.identity();
        scale.set(0,0, x);
        scale.set(1,1, y);
        scale.set(2,2, z);
    }

    public void setTranslation(float x, float y, float z) {
        translation.identity();
        translation.set(3, 0, x);
        translation.set(3, 1, y);
        translation.set(3, 2, z);
    }

    public void setRotation(float a) {
        rotation.identity();
        rotation.set(0,0, Math.cos(a));
        rotation.set(1,0, -Math.sin(a));
        rotation.set(0,1, Math.sin(a));
        rotation.set(1,1, Math.cos(a));
    }

    public void calculateMatrix() {
        this.transform = this.translation.mul(this.rotation.mul(this.scale));
    }
}
