package com.github.syr0ws.fallenkingdom.game.model.cycle;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycle.types.StartingCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycle.types.WaitingCycle;
import org.bukkit.plugin.Plugin;

public class FKCycleFactory implements GameCycleFactory {

    private final GameModel game;
    private final Plugin plugin;

    public FKCycleFactory(GameModel game, Plugin plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public GameCycle getCycle(GameState state) {

        switch (state) {
            case WAITING:
                return new WaitingCycle(this.plugin, this.game);
            case STARTING:
                return new StartingCycle(this.plugin, this.game);
            case RUNNING:
                return null;
            case FINISHED:
                return null;
            default:
                throw new IllegalArgumentException(String.format("No instance found for '%s'.", state.name()));
        }
    }
}
