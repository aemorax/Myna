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
    private final Queue<IDrawable> drawables;
    private int width;
    private int height;
    private Color color;
    private boolean requiresRedraw = false;
    private View view;

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
        this.setView(new View());
        this.addEventListener(Event.TRANSFORM_CHANGE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                setRequiresRedraw(true);
            }
        });
    }

    public boolean getRequiresRedraw() {
        return this.requiresRedraw;
    }

    public void setRequiresRedraw(boolean requiresRedraw) {
        this.requiresRedraw = requiresRedraw;
    }

    public void resizeAll() {
        Stack<DisplayObjectContainer> stack = new Stack<>();
        DisplayObjectContainer current = this;
        stack.push(current);

        while (!stack.isEmpty()) {
            current = stack.pop();
            for (DisplayObject displayObject : current.getChildren()) {
                displayObject.recalculateMatrix();
                if (displayObject instanceof DisplayObjectContainer) {
                    DisplayObjectContainer d = (DisplayObjectContainer) displayObject;
                    if (d.getChildrenCount() > 0)
                        stack.push(d);
                }
            }
        }
    }

    @Override
    public DisplayObject addChild(DisplayObject child) {
        return super.addChild(child);
    }

    @Override
    public DisplayObject removeChild(DisplayObject child) {
        return super.removeChild(child);
    }

    @Override
    public DisplayObject removeChildAt(int index) {
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
            } catch (ConcurrentModificationException e) {
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
        for (IDrawable drawable : drawables) {
            drawable.draw(this.myna.renderer.getCounter());
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
        recalculateMatrix();
    }

    public void setHeight(int newHeight) {
        this.height = newHeight;
        recalculateMatrix();
    }

    public void setSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        recalculateMatrix();
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view) {
        if (this.view != null)
            this.view.removeEventListeners(Event.TRANSFORM_CHANGE);
        view.addEventListener(Event.TRANSFORM_CHANGE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                EventListener.super.onEvent(event);
                recalculateMatrix();
            }
        });
        this.view = view;
    }

    @Override
    protected void recalculateMatrix() {
        recalculateStageMatrix();
    }

    private void recalculateStageMatrix() {
        this.transform.identity();
        this.transform.ortho(0, width, height, 0, -1, 1);
        this.transform.mul(this.view.transform);
    }
}