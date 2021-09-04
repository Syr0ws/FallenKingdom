package com.github.syr0ws.fallenkingdom.plugin.game.view;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.game.view.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.GameRunningViewHandler;
import com.github.syr0ws.fallenkingdom.plugin.game.view.global.FKGameView;
import com.github.syr0ws.fallenkingdom.plugin.game.view.global.FKTeamView;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.sdk.game.view.AbstractGameViewHandler;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;

public class FKGameViewHandler extends AbstractGameViewHandler {

    public FKGameViewHandler(FKGame game, FKModel model) {
        super(game, model);
    }

    @Override
    protected void registerViewHandlers() {
        super.registerViewHandlers();

        // Initializing variables.
        FKGame game = this.getGame();
        FKModel model = this.getModel();
        DisplayManager manager = this.getDisplayManager();

        // Registering view handlers.
        super.addViewHandler(new GameRunningViewHandler(game, model, manager));
    }

    @Override
    protected void registerViews() {
        super.registerViews();

        // Initializing variables.
        FKGame game = this.getGame();
        LangService service = game.getLangService();
        DisplayManager manager = this.getDisplayManager();

        // Registering views.
        super.addView(new FKTeamView(game, manager));
        super.addView(new FKGameView(game, service));
    }

    @Override
    public void enable() {

        // DisplayManager must be setup first as it is used in ViewHandler.
        this.setupDisplayManager();

        super.enable();
    }

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }

    @Override
    public FKGame getGame() {
        return (FKGame) super.getGame();
    }

    private void setupDisplayManager() {

        DisplayManager manager = super.getDisplayManager();

        for(GameDisplayEnum value : GameDisplayEnum.values())
            manager.loadDisplays(value.getPath());
    }
}
