package com.nevergarden.myna.display;

import com.nevergarden.myna.geom.TransformMatrix;
import com.nevergarden.myna.gfx.Quad;
import com.nevergarden.myna.interfaces.Container;

public class DisplayObject extends DisplayObjectContainer {
    private float x, y, pivotX, pivotY, scaleX, scaleY, skewX, skewY, rotation, alpha;
    private boolean visible;

    public TransformMatrix localMatrix;

    public Quad quad;

    public DisplayObject() {
        this(null);
    }

    public DisplayObject(Container parent) {
        super(parent);
        x = y = pivotY = pivotX = rotation = skewX = skewY = 0.0f;
        scaleX = scaleY = alpha = 1.0f;
        visible = true;

        this.localMatrix = new TransformMatrix();
        this.quad = new Quad(new float[]{1,0,0,1}, 200, 300);
        this.quad.setMatrix(localMatrix.transform);
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.localMatrix.setTranslation(this.x, this.y, 0);
        this.localMatrix.calculateMatrix();
        this.quad.setMatrix(this.localMatrix.transform);
    }
}
