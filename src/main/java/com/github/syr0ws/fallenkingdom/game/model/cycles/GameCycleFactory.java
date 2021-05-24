package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.GameRunningCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.GameWaitingCycle;
import org.bukkit.plugin.Plugin;

public class GameCycleFactory {

    private final GameModel game;
    private final Plugin plugin;

    public GameCycleFactory(GameModel game, Plugin plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    public GameCycle getCycle(GameState state) {

        switch (state) {
            case WAITING:
                return new GameWaitingCycle(this.plugin, this.game);
            case RUNNING:
                return new GameRunningCycle(this.plugin, this.game);
            case FINISHED:
                return null;
            default:
                return null;
        }
    }
}
