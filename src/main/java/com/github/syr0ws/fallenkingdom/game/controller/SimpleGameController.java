package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import org.bukkit.plugin.Plugin;

public class SimpleGameController implements GameController {

    private final Plugin plugin;
    private final GameModel model;

    public SimpleGameController(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public void startGame() {

        if(this.model.getState() != GameState.WAITING)
            throw new GameException("Game already started.");

        this.model.setState(GameState.STARTING);
    }

    @Override
    public void stopGame() {

        GameState current = this.model.getState();
        GameState state = current == GameState.STARTING ? GameState.WAITING : GameState.FINISHED;

        this.model.setState(state);
    }
}
