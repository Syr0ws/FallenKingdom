package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.controller.cycle.types.GameLoadingCycle;

public class FKLoadingCycle extends GameLoadingCycle {

    public FKLoadingCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);
    }

    @Override
    public void enable() {
        super.enable();
        super.done();
    }
}
