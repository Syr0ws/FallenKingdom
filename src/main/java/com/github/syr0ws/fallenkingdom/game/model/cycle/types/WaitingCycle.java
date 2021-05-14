package com.github.syr0ws.fallenkingdom.game.model.cycle.types;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.listeners.PreRunningListener;
import org.bukkit.plugin.Plugin;

public class WaitingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;

    public WaitingCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
    }

    @Override
    public void start() {
        this.manager.addListener(new PreRunningListener(this.plugin, this.game));
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
