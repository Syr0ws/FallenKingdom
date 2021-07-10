package com.github.syr0ws.fallenkingdom.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.GameBoard;
import com.github.syr0ws.fallenkingdom.scoreboards.SpectatorBoard;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.game.model.mode.ModeType;
import com.github.syr0ws.universe.modules.ModuleEnum;
import com.github.syr0ws.universe.modules.ModuleService;
import com.github.syr0ws.universe.modules.scoreboard.Scoreboard;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardManager;
import com.github.syr0ws.universe.modules.scoreboard.ScoreboardModule;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SpectatorMode extends FKMode {

    private final FKModel model;

    public SpectatorMode(FKGame game) {
        super(game);

        this.model = game.getGameModel();
    }

    @Override
    public void enable(Player player) {

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(this.model.getSpawn());

        // Handling scoreboard.
        this.setScoreboard(player);
    }

    @Override
    public void disable(Player player) {

        // Handling scoreboard.
        this.removeScoreboard(player);
    }

    @Override
    public ModeType getType() {
        return DefaultModeType.SPECTATOR;
    }

    private ScoreboardManager getScoreboardManager() throws GameException {

        ModuleService service = this.getGame().getModuleService();

        Optional<ScoreboardModule> optional = service.getModule(ModuleEnum.SCOREBOARD_MODULE.getName(), ScoreboardModule.class);

        if(!optional.isPresent())
            throw new GameException("ScoreboardModule not enabled.");

        return optional.get().getScoreboardManager();
    }

    private void setScoreboard(Player player) {

        FKPlayer fkPlayer = this.model.getPlayer(player.getUniqueId());

        try {

            ScoreboardManager manager = this.getScoreboardManager();

            Scoreboard scoreboard = fkPlayer.isPlaying() ?
                    new GameBoard(manager, player, this.getGame()) : new SpectatorBoard(manager, player, this.getGame());

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
}
