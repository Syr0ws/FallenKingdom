package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.listeners.GamePreRunningListener;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import org.bukkit.plugin.Plugin;

public class GameWaitingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;

    public GameWaitingCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
    }

    @Override
    public void start() {
        this.manager.addListener(new GamePreRunningListener());
    }

    @Override
    public void stop() {
        this.manager.removeListeners();
    }

    @Override
    public GameState getState() {
        return GameState.WAITING;
    }
}
