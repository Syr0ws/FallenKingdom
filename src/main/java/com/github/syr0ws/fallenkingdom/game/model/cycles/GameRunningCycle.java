package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.listeners.GameBlockListener;
import com.github.syr0ws.fallenkingdom.listeners.GamePlayerListener;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.listeners.ListenerManager;

public class GameRunningCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    public GameRunningCycle(FKGame game, FKController controller, FKModel model) {
        super(game);

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.controller = controller;
        this.model = model;
    }

    @Override
    public void load() {
        super.load();

        // Registering cycle listeners.
        this.registerListeners();

        // Handling captures.
        this.controller.getCaptureManager().enable();
    }

    @Override
    public void unload() {
        super.unload();

        // Unregistering listeners.
        super.getListenerManager().removeListeners();

        // Handling captures.
        this.controller.getCaptureManager().disable();
    }

    @Override
    public FKGame getGame() {
        return (FKGame) super.getGame();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();

        manager.addListener(new GamePlayerListener(this.getGame()));
        manager.addListener(new GameBlockListener(this.getGame()));
    }
}
