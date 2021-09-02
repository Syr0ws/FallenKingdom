package com.github.syr0ws.fallenkingdom.plugin.game.view.states;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.AssaultsView;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.EndView;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.NetherView;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.running.PvPView;
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

        super.addView(new AssaultsView(this.model, this.manager));
        super.addView(new PvPView(this.model, this.manager));
        super.addView(new NetherView(this.model, this.manager));
        super.addView(new EndView(this.model, this.manager));

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
