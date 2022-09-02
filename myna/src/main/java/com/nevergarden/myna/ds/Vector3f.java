package com.nevergarden.myna.ds;

/**
 * XYZ Float Vector.
 */
public class Vector3f {
    /** X Value */
    public float x;
    /** Y Value */
    public float y;
    /** Z Value */
    public float z;

    /**
     * Zero Constructor.
     */
    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Default Constructor.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
