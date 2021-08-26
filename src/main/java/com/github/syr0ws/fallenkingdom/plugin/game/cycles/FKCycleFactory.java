package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.universe.api.game.controller.cycle.GameCycle;
import com.github.syr0ws.universe.api.game.controller.cycle.GameCycleFactory;
import com.github.syr0ws.universe.api.game.model.GameState;

public class FKCycleFactory implements GameCycleFactory {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public FKCycleFactory(FKGame game, FKModel model, FKController controller) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        this.game = game;
        this.model = model;
        this.controller = controller;
    }

    @Override
    public GameCycle getGameCycle(GameState state) {

        switch (state) {
            case LOADING:
                return new FKLoadingCycle(this.game, this.model, this.controller);
            case WAITING:
                return new FKWaitingCycle(this.game, this.model, this.controller);
            case STARTING:
                return new FKStartingCycle(this.game, this.model, this.controller);
            case RUNNING:
                return new FKRunningCycle(this.game, this.model, this.controller);
            case FINISHED:
                return new FKFinishCycle(this.game, this.model, this.controller);
            default:
                throw new IllegalArgumentException(String.format("No GameCycle found for state '%s'.", state.name()));
        }
    }
}
