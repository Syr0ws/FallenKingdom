package com.github.syr0ws.fallenkingdom.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.views.GameActionBar;
import com.github.syr0ws.fallenkingdom.views.PlayerNameView;
import com.github.syr0ws.fallenkingdom.views.scoreboards.PlayerGameBoard;
import com.github.syr0ws.universe.commons.mode.types.PlayingMode;
import com.github.syr0ws.universe.commons.modules.ModuleEnum;
import com.github.syr0ws.universe.commons.modules.ModuleService;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.view.ViewModule;
import com.github.syr0ws.universe.commons.modules.view.ViewService;
import com.github.syr0ws.universe.commons.modules.view.impl.DefaultViewType;
import com.github.syr0ws.universe.commons.modules.view.views.ActionBarView;
import com.github.syr0ws.universe.commons.modules.view.views.NameView;
import com.github.syr0ws.universe.commons.modules.view.views.ScoreboardView;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.game.model.GameModel;
import org.bukkit.entity.Player;

public class FKPlayingMode extends PlayingMode {

    private final FKGame game;

    public FKPlayingMode(GameModel model, FKGame game) {
        super(model);

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
    }

    @Override
    public void enable(Player player) {
        super.enable(player);

        FKTeamPlayer teamPlayer = this.getTeamPlayer(player);
        FKTeam team = teamPlayer.getTeam();

        player.setPlayerListName(String.format("%s %s", team.getDisplayName(), player.getName()));

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

    private FKTeamPlayer getTeamPlayer(Player player) {

        FKModel model = this.getModel();

        return model.getTeamPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalArgumentException("Player is not a FKTeamPlayer."));
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

            this.setScoreboard(player, viewService, langService);
            this.setGameActionBar(player, viewService, langService);
            this.setNameView(player, viewService);

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

        PlayerGameBoard board = new PlayerGameBoard(player, langService, this.getModel());

        // Setting game scoreboard.
        viewService.getViewHandler(DefaultViewType.SCOREBOARD, ScoreboardView.class).addView(player, board);
    }

    private void setGameActionBar(Player player, ViewService viewService, LangService langService) {

        GameActionBar actionBar = new GameActionBar(this.getTeamPlayer(player), this.getModel(), langService);

        // Setting game action bar.
        viewService.getViewHandler(DefaultViewType.ACTION_BAR, ActionBarView.class).addView(player, actionBar);
    }

    private void setNameView(Player player, ViewService service) {

        FKTeamPlayer teamPlayer = this.getTeamPlayer(player);

        PlayerNameView view = new PlayerNameView(teamPlayer);

        service.getViewHandler(DefaultViewType.NAME, NameView.class).addView(player, view);
    }
}
