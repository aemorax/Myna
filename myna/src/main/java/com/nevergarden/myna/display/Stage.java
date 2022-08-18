package com.nevergarden.myna.display;

import android.util.Log;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventDispatcher;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.ResizeEventData;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.interfaces.IDrawable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class Stage extends DisplayObjectContainer {
    private Myna myna;
    private int width;
    private int height;
    private Color color;
    private final Queue<IDrawable> drawables;
    public Stage(Myna myna, Color color) {
        super();
        this.myna = myna;
        this.setColor(color);
        this.drawables = new LinkedBlockingQueue<>();
        this.myna.eventDispatcher.addEventListener(Event.RESIZE, new EventListener() {
            @Override
            public void onEvent(IEvent event) {
                ResizeEventData data = (ResizeEventData) event.getData();
                setSize(data.width, data.height);
                resizeAll();
            }
        });
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

    public void addAll() {
        synchronized (this.myna.renderer.thread) {
            Log.d("Mnnn", ""+this.children.size());
            Stack<DisplayObjectContainer> stack = new Stack<>();
            DisplayObjectContainer current = this;
            stack.push(current);

            while (!stack.isEmpty()) {
                current = stack.pop();
                for (DisplayObject displayObject : current.getChildren()) {
                    this.drawables.add(displayObject);
                    if (displayObject instanceof DisplayObjectContainer) {
                        DisplayObjectContainer d = (DisplayObjectContainer) displayObject;
                        if (d.getChildrenCount() > 0)
                            stack.push(d);
                    }
                }
            }
        }
    }
    public void drawAll() {
        while(!drawables.isEmpty()) {
            Objects.requireNonNull(drawables.poll()).draw();
        }
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
        this.localMatrix.identity();
        this.localMatrix.ortho(0, width, 0, height, -1, 1);
        float[] m = new float[16];
        this.localMatrix.get(m);
        Log.d("Myna", "Stage: " + Arrays.toString(m) + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}