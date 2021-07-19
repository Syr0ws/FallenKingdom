package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.listeners.WaitingCycleListener;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.listeners.ListenerManager;

public class GameWaitingCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    public GameWaitingCycle(FKGame game, FKController controller, FKModel model) {
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
    }

    @Override
    public void unload() {
        super.unload();

        // Unregistering listeners.
        super.getListenerManager().removeListeners();
    }

    @Override
    public FKGame getGame() {
        return (FKGame) super.getGame();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        manager.addListener(new WaitingCycleListener(this.controller, this.model, this.getGame().getLangService()));
    }
}
