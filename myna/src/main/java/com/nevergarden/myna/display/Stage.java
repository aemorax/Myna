package com.nevergarden.myna.display;

import com.nevergarden.myna.interfaces.Container;
import com.nevergarden.myna.interfaces.IDrawable;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Stage extends DisplayObjectContainer {
    private Queue<IDrawable> drawables;
    public Stage() {
        super(null);
        this.drawables = new LinkedBlockingQueue<>();
    }

    public void addAll() {
        for (Container container: this.children) {
            if(container instanceof DisplayObject) {
                DisplayObject c = (DisplayObject) container;
                this.drawables.add(c.quad);
            }
        }
    }

    public void drawAll() {
        while(!drawables.isEmpty()) {
            Objects.requireNonNull(drawables.poll()).draw();
        }
    }
}