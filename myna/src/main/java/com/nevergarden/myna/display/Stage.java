package com.nevergarden.myna.display;

import android.opengl.GLES20;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.ResizeEventData;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.interfaces.IDrawable;

import java.util.ConcurrentModificationException;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Stage extends DisplayObjectContainer {
    private final Myna myna;
    private int width;
    private int height;
    private Color color;
    private boolean requiresRedraw = false;
    private final Queue<IDrawable> drawables;
    public Stage(Myna myna, Color color) {
        super();
        this.myna = myna;
        this.setColor(color);
        this.drawables = new ConcurrentLinkedQueue<>();
        this.myna.eventDispatcher.addEventListener(Event.RESIZE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                ResizeEventData data = (ResizeEventData) event.getData();
                setSize(data.width, data.height);
                resizeAll();
            }
        });
    }

    public void setRequiresRedraw(boolean requiresRedraw) {
        this.requiresRedraw = requiresRedraw;
        if(requiresRedraw)
            this.addAll();
    }

    public void resizeAll() {
        Stack<DisplayObjectContainer> stack = new Stack<>();
        DisplayObjectContainer current = this;
        stack.push(current);

        while(!stack.isEmpty()) {
            current = stack.pop();
            for (DisplayObject displayObject : current.getChildren()) {
                displayObject.recalculateMatrix();
                if(displayObject instanceof DisplayObjectContainer) {
                    DisplayObjectContainer d = (DisplayObjectContainer) displayObject;
                    if(d.getChildrenCount() > 0)
                        stack.push(d);
                }
            }
        }
    }

    @Override
    public DisplayObject addChild(DisplayObject child) {
        DisplayObject o = super.addChild(child);
        this.setRequiresRedraw(true);
        return o;
    }

    @Override
    public DisplayObject removeChild(DisplayObject child) {
        DisplayObject o = super.removeChild(child);
        this.setRequiresRedraw(true);
        return o;
    }

    @Override
    public DisplayObject removeChildAt(int index) {
        this.setRequiresRedraw(true);
        return super.removeChildAt(index);
    }

    public void addAll() {
        drawables.clear();
        Stack<DisplayObjectContainer> stack = new Stack<>();
        DisplayObjectContainer current = this;
        stack.push(current);

        while (!stack.isEmpty()) {
            current = stack.pop();
            try {
                for (DisplayObject displayObject : current.getChildren()) {
                    this.drawables.add(displayObject);
                    if (displayObject instanceof DisplayObjectContainer) {
                        DisplayObjectContainer d = (DisplayObjectContainer) displayObject;
                        if (d.getChildrenCount() > 0)
                            stack.push(d);
                    }
                }
            }
            catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
    }
    public void drawAll() {
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        for (IDrawable drawable: drawables) {
            drawable.draw();
        }
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public Color getColor() {
        return this.color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
        recalculateStageMatrix();
    }
    public void setHeight(int newHeight) {
        this.height = newHeight;
        recalculateStageMatrix();
    }
    public void setSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        recalculateStageMatrix();
    }

    @Override
    protected void recalculateMatrix() {
        recalculateStageMatrix();
    }

    private void recalculateStageMatrix() {
        this.transform.identity();
        this.transform.ortho(0, width, height, 0, -1, 1);
        float[] m = new float[16];
        this.transform.get(m);
    }
}