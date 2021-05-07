package com.github.syr0ws.fallenkingdom.game.cycle;

import com.github.syr0ws.fallenkingdom.game.GameModel;
import com.github.syr0ws.fallenkingdom.game.GameState;
import org.bukkit.plugin.Plugin;

public class GameCycleFactory {

    private final Plugin plugin;
    private final GameModel model;

    public GameCycleFactory(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    public static GameCycle getCycle(GameState state) {

        switch (state) {
            case WAITING:
                return null;
            case STARTING:
                return null;
            case FINISHED:
                return null;
            case RUNNING:
                return null;
            default:
                throw new IllegalArgumentException(String.format("No instance found for '%s'.", state.name()));
        }
    }
}
