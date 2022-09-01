package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;

import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject {
    protected final ArrayList<DisplayObject> children;

    public DisplayObjectContainer() {
        super();
        if (!com.nevergarden.myna.BuildConfig.DEBUG)
            throw new AbstractMethodError();

        this.children = new ArrayList<>();
    }

    public DisplayObjectContainer getParent() {
        return this.parent;
    }

    public void setParent(DisplayObjectContainer container) {
        this.parent = container;
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public DisplayObject addChild(DisplayObject child) {
        if (this.children.contains(child))
            return null;
        this.children.add(child);
        child.setParent(this);
        child.recalculateMatrix();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    public DisplayObject addChildAt(int index, DisplayObject child) {
        if (this.children.contains(child))
            return null;
        this.children.add(index, child);
        child.setParent(this);
        child.recalculateMatrix();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    public Boolean contains(DisplayObject child) {
        return this.children.contains(child);
    }

    public DisplayObject getChildAt(int index) {
        return this.children.get(index);
    }

    public DisplayObject getChildByName(String name) {
        throw new UnsupportedOperationException();
    }

    public int getChildIndex(DisplayObject child) {
        return this.children.indexOf(child);
    }

    public DisplayObject removeChild(DisplayObject child) {
        if (this.children.remove(child))
            return child;
        return null;
    }

    public DisplayObject removeChildAt(int index) {
        if (this.children.size() == 0)
            return null;
        DisplayObject child = this.children.remove(index);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    public void removeChildren() {
        this.children.clear();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    public void swapChildren(DisplayObject child1, DisplayObject child2) {
        int index1 = this.getChildIndex(child1);
        int index2 = this.getChildIndex(child2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    public void swapChildrenAt(int index1, int index2) {
        DisplayObject child1 = this.getChildAt(index1);
        DisplayObject child2 = this.getChildAt(index2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    public ArrayList<DisplayObject> getChildren() {
        return this.children;
    }
}
