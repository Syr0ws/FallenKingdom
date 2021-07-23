package com.github.syr0ws.fallenkingdom.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.GameBoard;
import com.github.syr0ws.universe.commons.mode.types.PlayingMode;
import com.github.syr0ws.universe.commons.modules.ModuleEnum;
import com.github.syr0ws.universe.commons.modules.ModuleService;
import com.github.syr0ws.universe.commons.modules.scoreboard.Scoreboard;
import com.github.syr0ws.universe.commons.modules.scoreboard.ScoreboardManager;
import com.github.syr0ws.universe.commons.modules.scoreboard.ScoreboardModule;
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

        // Handling scoreboard.
        this.setScoreboard(player);
    }

    @Override
    public void disable(Player player) {
        super.disable(player);

        // Handling scoreboard.
        this.removeScoreboard(player);
    }

    private FKTeamPlayer getTeamPlayer(Player player) {

        Optional<? extends FKTeamPlayer> optional = this.getModel().getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new IllegalArgumentException("Player is not a FKTeamPlayer.");

        return optional.get();
    }

    private ScoreboardManager getScoreboardManager() throws GameException {

        ModuleService service = this.game.getModuleService();

        Optional<ScoreboardModule> optional = service.getModule(ModuleEnum.SCOREBOARD_MODULE.getName(), ScoreboardModule.class);

        if(!optional.isPresent())
            throw new GameException("ScoreboardModule not enabled.");

        return optional.get().getScoreboardManager();
    }

    private void setScoreboard(Player player) {

        try {

            ScoreboardManager manager = this.getScoreboardManager();
            Scoreboard scoreboard = new GameBoard(manager, player, this.game.getLangService(), this.getModel());
            scoreboard.set();

        } catch (GameException e) { e.printStackTrace(); }
    }

    private void removeScoreboard(Player player) {

        try {

            ScoreboardManager manager = this.getScoreboardManager();

            Optional<? extends Scoreboard> optional = manager.getScoreboard(player);
            optional.ifPresent(Scoreboard::remove);

        } catch (GameException e) { e.printStackTrace(); }
    }

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }
}
