package com.github.syr0ws.fallenkingdom.plugin.game.view.states;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.EndNotifier;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.NetherNotifier;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.PvPNotifier;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKTeamWinListener;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.api.game.model.GameState;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.view.AbstractGameStateViewHandler;

public class GameRunningViewHandler extends AbstractGameStateViewHandler {

    private final FKModel model;
    private final DisplayManager manager;

    public GameRunningViewHandler(Game game, FKModel model, DisplayManager manager) {
        super(game);

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.model = model;
        this.manager = manager;
    }

    @Override
    public void enable() {

        Game game = this.getGame();

        super.addView(new AssaultsNotifier(this.model, this.manager));
        super.addView(new PvPNotifier(this.model, this.manager));
        super.addView(new NetherNotifier(this.model, this.manager));
        super.addView(new EndNotifier(this.model, this.manager));

        super.addView(new FKTeamWinListener(game, this.manager));

        super.enable();
    }

    @Override
    public void disable() {
        super.removeViews();
        super.disable();
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }
}
