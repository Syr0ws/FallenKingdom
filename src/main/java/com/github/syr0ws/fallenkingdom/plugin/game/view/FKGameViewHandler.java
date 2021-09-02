package com.github.syr0ws.fallenkingdom.plugin.game.view;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.game.view.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.game.view.states.GameRunningViewHandler;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKListener;
import com.github.syr0ws.fallenkingdom.plugin.listeners.TeamListener;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.sdk.game.view.AbstractGameViewHandler;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;

public class FKGameViewHandler extends AbstractGameViewHandler {

    public FKGameViewHandler(FKGame game, FKModel model) {
        super(game, model);
    }

    @Override
    public void enable() {

        // DisplayManager must be setup first as it is used in ViewHandler.
        this.setupDisplayManager();

        // Registering handlers.
        this.registerViewHandlers();

        // Registering game views which are views used in all the game cycles.
        this.registerGameViews();

        super.enable();
    }

    @Override
    public void disable() {
        super.removeViews();
        super.removeViewHandlers();
        super.disable();
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

    private void registerViewHandlers() {

        // Initializing variables.
        FKGame game = this.getGame();
        FKModel model = this.getModel();
        DisplayManager manager = this.getDisplayManager();

        // Registering view handlers.
        super.addViewHandler(new GameRunningViewHandler(game, model, manager));
    }

    private void registerGameViews() {

        FKGame game = this.getGame();
        LangService service = game.getLangService();
        DisplayManager manager = this.getDisplayManager();

        super.addView(new TeamListener(game, manager));
        super.addView(new FKListener(game, service));
    }
}
