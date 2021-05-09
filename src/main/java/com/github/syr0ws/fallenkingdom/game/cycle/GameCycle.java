package com.github.syr0ws.fallenkingdom.game.cycle;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GameCycle implements AttributeObservable {

    private final List<AttributeObserver> observers;
    private final List<Listener> listeners;
    private boolean finished;

    public GameCycle() {
        this.observers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.finished = false;
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

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void clearListeners() {
        this.listeners.clear();
    }

    public void registerListeners(Plugin plugin) {
        PluginManager manager = Bukkit.getPluginManager();
        this.listeners.forEach(listener -> manager.registerEvents(listener, plugin));
    }

    public void unregisterListeners() {
        this.listeners.forEach(HandlerList::unregisterAll);
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

    public List<Listener> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }
}
