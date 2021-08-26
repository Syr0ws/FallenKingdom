package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.controller.cycle.types.GameFinishCycle;
import com.github.syr0ws.universe.sdk.game.mode.DefaultModeType;

public class FKFinishCycle extends GameFinishCycle {

    public FKFinishCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);
    }

    @Override
    public void enable() {
        super.enable();

        // Setting all the online players in spectator.
        this.getModel().getOnlinePlayers().stream()
                .filter(player -> player.getModeType() == DefaultModeType.PLAYING)
                .forEach(player -> this.getController().setMode(player, DefaultModeType.SPECTATOR));
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
}
