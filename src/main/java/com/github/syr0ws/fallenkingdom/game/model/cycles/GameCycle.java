package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameCycleAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GameCycle implements AttributeObservable {

    private boolean finished;
    private final List<AttributeObserver> observers = new ArrayList<>();

    public abstract void start();

    public abstract void stop();

    public abstract GameState getState();

    @Override
    public void notifyChange(Attribute attribute) {
        new ArrayList<>(this.observers).stream()
                .filter(observer -> observer.observed().contains(attribute))
                .forEach(observer -> observer.onUpdate(attribute));
    }

    @Override
    public void addObserver(AttributeObserver observer) {
        if(!this.observers.contains(observer)) this.observers.add(observer);
    }

    @Override
    public void removeObserver(AttributeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public Collection<AttributeObserver> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }

    public void finish() {
        this.finished = true;
        this.notifyChange(GameCycleAttribute.FINISH_STATE);
    }

    public boolean isFinished() {
        return this.finished;
    }
}
