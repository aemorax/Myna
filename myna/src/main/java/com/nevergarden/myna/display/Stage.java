package com.nevergarden.myna.display;

import android.opengl.GLES20;

import com.nevergarden.myna.core.Myna;
import com.nevergarden.myna.events.Event;
import com.nevergarden.myna.events.EventListener;
import com.nevergarden.myna.events.IEvent;
import com.nevergarden.myna.events.ResizeEventData;
import com.nevergarden.myna.ds.Color;

import java.util.ConcurrentModificationException;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Root of the display tree.
 *
 * Display objects added to stage can only be rendered, every Myna instance has
 * one Stage connected to it which can be replaced by another stage on demand.
 */
public class Stage extends DisplayObjectContainer {
    private final Myna myna;
    private final Queue<Drawable> drawables;
    private int width;
    private int height;
    private Color color;
    private boolean requiresRedraw = false;
    private View view;

    /**
     * Default Constructor.
     *
     * Create a new stage with given color as it's background color.
     */
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

    /**
     * A method that indicates if the hierarchy has changed or not so the frame should be redrawn.
     */
    public boolean getRequiresRedraw() {
        return this.requiresRedraw;
    }

    /**
     * Forces a redraw if true is set.
     */
    public void setRequiresRedraw(boolean requiresRedraw) {
        this.requiresRedraw = requiresRedraw;
    }

    /**
     * Resizes every object in the hierarchy to fit the new stage size.
     */
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

    /**
     * Traverse the tree for every drawable to create render tree.
     */
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

    /**
     * Called by renderer to draw all the drawables in this stage.
     *
     * @implNote This should not be here and myst move to Renderer side.
     */
    public void drawAll() {
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        for (Drawable drawable : drawables) {
            drawable.draw(this.myna.renderer.getCounter());
        }
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    /**
     * Gets current background color.
     * @return Current Background Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets Background Color
     * @param color color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets width of stage.
     */
    public void setWidth(int newWidth) {
        this.width = newWidth;
        recalculateMatrix();
    }

    /**
     * Sets height of stage.
     */
    public void setHeight(int newHeight) {
        this.height = newHeight;
        recalculateMatrix();
    }

    /**
     * Sets size of stage
     * @param newWidth width
     * @param newHeight height
     */
    public void setSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        recalculateMatrix();
    }

    /**
     * Gets current view of the stage.
     * @return View current view
     */
    public View getView() {
        return this.view;
    }

    /**
     * Sets a new View for this stage.
     * @param view new view
     */
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