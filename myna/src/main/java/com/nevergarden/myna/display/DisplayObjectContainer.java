package com.nevergarden.myna.display;

import com.nevergarden.myna.events.Event;

import java.util.ArrayList;

/**
 * Base class for collection of DisplayObjects.
 *
 * DisplayObjectContainer does not have anything drawable for itself but it
 * can contain many drawables inside, it's bounds are calulated by it's children.
 *
 * Using DisplayObjectContainer is not recommended as long as it has a lot of
 * overhead for creating new arrays, use it when you want to group
 * DisplayObjects together.
 *
 * @see DisplayObject
 */
public class DisplayObjectContainer extends DisplayObject {
    /**
     * Array of children in this container.
     */
    protected final ArrayList<DisplayObject> children;

    public DisplayObjectContainer() {
        super();
        this.children = new ArrayList<>();
    }

    /**
     * Get current container parent
     */
    public DisplayObjectContainer getParent() {
        return this.parent;
    }

    /**
     * Sets current container parent.
     */
    public void setParent(DisplayObjectContainer container) {
        this.parent = container;
    }

    /**
     * Current children count.
     * @return integer
     */
    public int getChildrenCount() {
        return this.children.size();
    }

    /**
     * Adds a new child to itself.
     * @param child
     * @return
     */
    public DisplayObject addChild(DisplayObject child) {
        if (this.children.contains(child))
            return null;
        this.children.add(child);
        child.setParent(this);
        child.recalculateMatrix();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    /**
     * Same as addChild method but with given index.
     */
    public DisplayObject addChildAt(int index, DisplayObject child) {
        if (this.children.contains(child))
            return null;
        this.children.add(index, child);
        child.setParent(this);
        child.recalculateMatrix();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    /**
     * Checks if given DisplayObject already exists in this container.
     */
    public Boolean contains(DisplayObject child) {
        return this.children.contains(child);
    }

    /**
     * Get child at given index.
     */
    public DisplayObject getChildAt(int index) {
        return this.children.get(index);
    }

    /**
     * Gets index of a given DisplayObject.
     */
    public int getChildIndex(DisplayObject child) {
        return this.children.indexOf(child);
    }

    /**
     * Removes a child from it's tree.
     */
    public DisplayObject removeChild(DisplayObject child) {
        if (this.children.remove(child))
            return child;
        return null;
    }

    /**
     * Removes a child from given index.
     */
    public DisplayObject removeChildAt(int index) {
        if (this.children.size() == 0)
            return null;
        DisplayObject child = this.children.remove(index);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
        return child;
    }

    /**
     * Clears the container.
     */
    public void removeChildren() {
        this.children.clear();
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    /**
     * Swap children in container.
     */
    public void swapChildren(DisplayObject child1, DisplayObject child2) {
        int index1 = this.getChildIndex(child1);
        int index2 = this.getChildIndex(child2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    /**
     * Swap children with their given indices.
     */
    public void swapChildrenAt(int index1, int index2) {
        DisplayObject child1 = this.getChildAt(index1);
        DisplayObject child2 = this.getChildAt(index2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
        this.dispatchEventWith(Event.TRANSFORM_CHANGE);
    }

    /**
     * Get a list of all children in this container.
     */
    public ArrayList<DisplayObject> getChildren() {
        return this.children;
    }
}
