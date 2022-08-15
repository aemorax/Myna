package com.nevergarden.myna.display;

import com.nevergarden.myna.interfaces.Container;
import com.nevergarden.myna.interfaces.IDrawable;

import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class Stage extends DisplayObjectContainer {
    private Queue<IDrawable> drawables;
    public Stage() {
        super(null);
        this.drawables = new LinkedBlockingQueue<>();
    }

    public void addAll() {
        Stack<Container> stack = new Stack<>();
        Container current = this;
        stack.push(current);

        while(!stack.isEmpty()) {
            current = stack.pop();
            for(Container container: current.getChildren()) {
                DisplayObject c = (DisplayObject) container;
                this.drawables.add(c.quad);
                if(container.getChildrenCount() > 0)
                    stack.push(container);
            }
        }
    }

    public void drawAll() {
        while(!drawables.isEmpty()) {
            Objects.requireNonNull(drawables.poll()).draw();
        }
    }
}