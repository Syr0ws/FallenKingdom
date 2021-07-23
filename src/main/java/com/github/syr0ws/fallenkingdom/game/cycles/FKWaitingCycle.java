package com.github.syr0ws.fallenkingdom.game.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.listeners.FKWaitingListener;
import com.github.syr0ws.universe.commons.cycle.types.WaitingCycle;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.controller.GameController;
import com.github.syr0ws.universe.sdk.game.model.GameModel;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;

public class FKWaitingCycle extends WaitingCycle {

    public FKWaitingCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);
    }

    @Override
    public void load() {
        super.load();

        // Handling listeners.
        this.registerListeners();
    }

    @Override
    public FKGame getGame() {
        return (FKGame) super.getGame();
    }

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }

    @Override
    public FKController getController() {
        return (FKController) super.getController();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        manager.addListener(new FKWaitingListener(this.getModel(), this.getController()));
    }
}
