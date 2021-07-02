package com.github.syr0ws.fallenkingdom.game.model.v2.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.v2.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.game.model.mode.ModeType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayingMode extends FKMode {

    private final FKModel model;

    public PlayingMode(FKGame game) {
        super(game);

        this.model = game.getGameModel();
    }

    @Override
    public void enable(Player player) {

        GamePlayer gamePlayer = this.model.getPlayer(player.getUniqueId());

        FKTeamPlayer teamPlayer = this.getTeamPlayer(player);
        FKTeam team = teamPlayer.getTeam();

        if(!gamePlayer.isPlaying()) {

            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setBedSpawnLocation(null);
            player.teleport(team.getBase().getSpawn());
        }

        player.setPlayerListName(String.format("%s %s", team.getDisplayName(), player.getName()));
    }

    @Override
    public void disable(Player player) {

    }

    @Override
    public ModeType getType() {
        return DefaultModeType.PLAYING;
    }

    private FKTeamPlayer getTeamPlayer(Player player) {

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new IllegalArgumentException("GamePlayer is not a FKTeamPlayer.");

        return optional.get();
    }
}
