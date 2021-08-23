package com.github.syr0ws.fallenkingdom.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.views.scoreboards.WaitingBoard;
import com.github.syr0ws.universe.api.game.model.GameException;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.api.modules.ModuleService;
import com.github.syr0ws.universe.sdk.game.mode.types.WaitingMode;
import com.github.syr0ws.universe.sdk.modules.ModuleEnum;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.view.ViewModule;
import com.github.syr0ws.universe.sdk.modules.view.ViewService;
import com.github.syr0ws.universe.sdk.modules.view.impl.DefaultViewType;
import com.github.syr0ws.universe.sdk.modules.view.views.ScoreboardView;
import org.bukkit.entity.Player;

public class FKWaitingMode extends WaitingMode {

    private final FKGame game;

    public FKWaitingMode(GameModel model, FKGame game) {
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

        LangService langService = this.game.getLangService();

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

        WaitingBoard board = new WaitingBoard(player, langService, this.getModel());

        // Setting game scoreboard.
        viewService.getViewHandler(DefaultViewType.SCOREBOARD, ScoreboardView.class).addView(player, board);
    }
}
