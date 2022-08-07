package com.nevergarden.myna.display;

import com.nevergarden.myna.interfaces.Container;

import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject implements Container {
    private final ArrayList<Container> children;

    public DisplayObjectContainer() {
        this.children = new ArrayList<>();
    }
    @Override
    public int getChildrenCount() {
        return this.children.size();
    }

    @Override
    public Container addChild(Container child) {
        if(this.children.contains(child))
            return null;
        this.children.add(child);
        return child;
    }

    @Override
    public Container addChildAt(int index, Container child) {
        if(this.children.contains(child))
            return null;
        this.children.add(index, child);
        return child;
    }

    @Override
    public Boolean contains(Container child) {
        return this.children.contains(child);
    }

    @Override
    public Container getChildAt(int index) {
        return this.children.get(index);
    }

    @Override
    public Container getChildByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getChildIndex(Container child) {
        return this.children.indexOf(child);
    }

    @Override
    public Container removeChild(Container child) {
        if(this.children.remove(child))
            return child;
        return null;
    }

    @Override
    public Container removeChildAt(int index) {
        return this.children.remove(index);
    }

    @Override
    public void removeChildren() {
        this.children.clear();
    }

    @Override
    public void swapChildren(Container child1, Container child2) {
        int index1 = this.getChildIndex(child1);
        int index2 = this.getChildIndex(child2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
    }

    @Override
    public void swapChildrenAt(int index1, int index2) {
        Container child1 = this.getChildAt(index1);
        Container child2 = this.getChildAt(index2);
        this.children.set(index1, child2);
        this.children.set(index2, child1);
    }
}
