package com.github.syr0ws.fallenkingdom.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListenerManager {

    private final Plugin plugin;
    private final List<Listener> listeners;

    public ListenerManager(Plugin plugin) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
    }

    private void registerListener(Listener listener) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listener, this.plugin);
    }

    private void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
        this.registerListener(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
        this.unregisterListener(listener);
    }

    public void removeListeners() {
        this.listeners.forEach(HandlerList::unregisterAll);
        this.listeners.clear();
    }

    public List<Listener> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }
}
