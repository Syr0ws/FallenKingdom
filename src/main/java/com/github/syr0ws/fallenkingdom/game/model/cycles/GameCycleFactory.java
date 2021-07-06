package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;

import java.util.Optional;

public class GameCycleFactory {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public GameCycleFactory(FKGame game, FKModel model, FKController controller) {

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

    public Optional<GameCycle> getCycle(GameState state) {

        FKModel model = this.game.getGameModel();
        FKController controller = this.game.getGameController();

        GameCycle cycle;

        switch (state) {
            case WAITING:
                cycle = new GameWaitingCycle(this.game, this.controller, this.model);
                break;
            case RUNNING:
                cycle = new GameRunningCycle(this.game, this.controller, this.model);
                break;
            case FINISHED:
                cycle = new GameFinishCycle(this.game, this.controller, this.model);
                break;
            default:
                cycle = null;
                break;
        }
        return Optional.ofNullable(cycle);
    }
}
