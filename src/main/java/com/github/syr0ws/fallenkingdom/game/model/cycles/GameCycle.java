package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.game.model.attributes.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.universe.attributes.AbstractAttributeObservable;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GameCycle extends AbstractAttributeObservable {

    private final Plugin plugin;
    private final ListenerManager listenerManager;
    private final TimerActionManager actionManager;
    private final List<AttributeObserver> observers = new ArrayList<>();

    private boolean finished;

    public GameCycle(Plugin plugin) {

        if(plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null.");

        this.plugin = plugin;
        this.listenerManager = new ListenerManager(plugin);
        this.actionManager = new TimerActionManager();
    }

    public abstract void load();

    public abstract void unload();

    public abstract void start();

    public abstract void stop();

    public void finish() {
        this.finished = true;
        this.notifyChange(GameCycleAttribute.FINISH_STATE);
    }

    public boolean isFinished() {
        return this.finished;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public ListenerManager getListenerManager() {
        return this.listenerManager;
    }

    public TimerActionManager getActionManager() {
        return this.actionManager;
    }

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
}
