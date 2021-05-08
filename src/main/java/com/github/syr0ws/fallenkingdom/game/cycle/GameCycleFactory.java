package com.github.syr0ws.fallenkingdom.game.cycle;

import com.github.syr0ws.fallenkingdom.game.cycle.impl.StartingCycle;
import com.github.syr0ws.fallenkingdom.game.cycle.impl.WaitingCycle;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import org.bukkit.plugin.Plugin;

public class GameCycleFactory {

    private final Plugin plugin;
    private final GameModel model;

    public GameCycleFactory(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    public GameCycle getCycle(GameState state) {

        switch (state) {
            case WAITING:
                return new WaitingCycle(this.model, this.plugin);
            case STARTING:
                return new StartingCycle(this.model, this.plugin);
            case RUNNING:
                return null;
            case FINISHED:
                return null;
            default:
                throw new IllegalArgumentException(String.format("No instance found for '%s'.", state.name()));
        }
    }
}
