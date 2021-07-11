package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;

public class GameFinishCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    public GameFinishCycle(FKGame game, FKController controller, FKModel model) {
        super(game);

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.controller = controller;
        this.model = model;
    }

    @Override
    public void start() {
        super.start();

        // Setting all the online players in spectator.
        this.model.getOnlinePlayers().stream()
                .filter(player -> player.getModeType() == DefaultModeType.PLAYING)
                .forEach(player -> this.controller.setMode(player, DefaultModeType.SPECTATOR));
    }
}
