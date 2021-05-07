package com.github.syr0ws.fallenkingdom.game.cycle;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GameCycle implements AttributeObservable {

    private boolean finished;
    private final List<AttributeObserver> observers;

    public GameCycle() {
        this.finished = false;
        this.observers = new ArrayList<>();
    }

    public abstract void start();

    public abstract void stop();

    public void setFinished(boolean finished) {
        this.finished = finished;
        this.notifyChange(GameCycleAttribute.FINISH_STATE);
    }

    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public void notifyChange(Attribute attribute) {
        this.observers.stream()
                .filter(observer -> observer.observed().contains(attribute))
                .forEach(observer -> observer.onUpdate(attribute));
    }

    @Override
    public void addObserver(AttributeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(AttributeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public Collection<AttributeObserver> getObservers() {
        return Collections.unmodifiableCollection(this.observers);
    }
}
