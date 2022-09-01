package com.nevergarden.myna.interfaces;

import java.util.ArrayList;

public interface Container {
    Container getParent();

    void setParent(Container container);

    int getChildrenCount();

    Container addChild(Container child);

    Container addChildAt(int index, Container child);

    Boolean contains(Container child);

    Container getChildAt(int index);

    Container getChildByName(String name);

    int getChildIndex(Container child);

    Container removeChild(Container child);

    Container removeChildAt(int index);

    void removeChildren();

    void swapChildren(Container child1, Container child2);

    void swapChildrenAt(int index1, int index2);

    ArrayList<Container> getChildren();
}