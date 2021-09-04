package com.github.syr0ws.fallenkingdom.plugin.modes;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.views.scoreboards.SpectatorGameBoard;
import com.github.syr0ws.universe.api.game.model.GameException;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.api.modules.ModuleService;
import com.github.syr0ws.universe.sdk.game.mode.types.SpectatorMode;
import com.github.syr0ws.universe.sdk.modules.ModuleEnum;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.view.ViewModule;
import com.github.syr0ws.universe.sdk.modules.view.ViewService;
import com.github.syr0ws.universe.sdk.modules.view.impl.DefaultViewType;
import com.github.syr0ws.universe.sdk.modules.view.views.ScoreboardView;
import org.bukkit.entity.Player;

public class FKSpectatorMode extends SpectatorMode {

    private final FKGame game;

    public FKSpectatorMode(GameModel model, FKGame game) {
        super(model);

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
    }

    @Override
    public void enable(Player player) {
        super.enable(player);

        // Setting views.
        this.setViews(player);
    }

    @Override
    public void disable(Player player) {
        super.disable(player);

        // Removing views.
        this.removeViews(player);
    }

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }

    private ViewModule getViewModule() throws GameException {

        ModuleService service = this.game.getModuleService();

        return service.getModule(ModuleEnum.VIEW_MODULE.getName(), ViewModule.class)
                .orElseThrow(() -> new GameException("ViewModule not enabled."));
    }

    private void setViews(Player player) {

        LangService langService = this.game.getServicesManager().load(LangService.class);

        try {

            ViewModule viewModule = this.getViewModule();
            ViewService viewService = viewModule.getViewService();

            // Setting game scoreboard.
            this.setScoreboard(player, viewService, langService);

        } catch (GameException e) { e.printStackTrace(); }
    }

    private void removeViews(Player player) {

        try {

            ViewModule viewModule = this.getViewModule();
            ViewService viewService = viewModule.getViewService();

            // Removing all views.
            viewService.getViewHandlers().stream()
                    .filter(handler -> handler.hasViews(player))
                    .forEach(handler -> handler.removeViews(player));

        } catch (GameException e) { e.printStackTrace(); }
    }

    private void setScoreboard(Player player, ViewService viewService, LangService langService) {

        SpectatorGameBoard board = new SpectatorGameBoard(player, langService, this.getModel());

        // Setting game scoreboard.
        viewService.getViewHandler(DefaultViewType.SCOREBOARD, ScoreboardView.class).addView(player, board);
    }
}
