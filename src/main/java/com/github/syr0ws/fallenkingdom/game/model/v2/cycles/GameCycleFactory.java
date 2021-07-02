package com.github.syr0ws.fallenkingdom.game.model.v2.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.v2.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.GameState;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;

import java.util.Optional;

public class GameCycleFactory {

    private final FKGame game;

    public GameCycleFactory(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
    }

    public Optional<GameCycle> getCycle(GameState state) {

        FKModel model = this.game.getGameModel();
        FKController controller = this.game.getGameController();

        GameCycle cycle;

        switch (state) {
            case WAITING:
                cycle = new GameWaitingCycle(this.game, controller, model);
                break;
            case RUNNING:
                cycle = new GameRunningCycle(this.game, controller, model);
                break;
            case FINISHED:
                cycle = new GameFinishCycle(this.game, controller, model);
                break;
            default:
                cycle = null;
                break;
        }
        return Optional.ofNullable(cycle);
    }
}
