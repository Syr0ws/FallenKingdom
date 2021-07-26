package com.github.syr0ws.fallenkingdom.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.GameBoard;
import com.github.syr0ws.universe.commons.mode.types.PlayingMode;
import com.github.syr0ws.universe.commons.modules.ModuleEnum;
import com.github.syr0ws.universe.commons.modules.ModuleService;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.view.ViewModule;
import com.github.syr0ws.universe.commons.modules.view.ViewService;
import com.github.syr0ws.universe.commons.modules.view.impl.DefaultViewType;
import com.github.syr0ws.universe.commons.modules.view.views.ScoreboardView;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.game.model.GameModel;
import org.bukkit.entity.Player;

import java.util.Optional;

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

    private FKTeamPlayer getTeamPlayer(Player player) {

        Optional<? extends FKTeamPlayer> optional = this.getModel().getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new IllegalArgumentException("Player is not a FKTeamPlayer.");

        return optional.get();
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
            viewService.getViewHandler(DefaultViewType.SCOREBOARD, ScoreboardView.class)
                    .addView(player, new GameBoard(player, langService, this.getModel()));

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

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }
}
