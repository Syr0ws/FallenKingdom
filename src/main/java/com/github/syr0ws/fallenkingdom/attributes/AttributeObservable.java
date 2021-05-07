package com.github.syr0ws.fallenkingdom.attributes;

import java.util.Collection;

public interface AttributeObservable {

    void notifyChange(Attribute attribute);

    void addObserver(AttributeObserver observer);

    void removeObserver(AttributeObserver observer);

    Collection<AttributeObserver> getObservers();
}
