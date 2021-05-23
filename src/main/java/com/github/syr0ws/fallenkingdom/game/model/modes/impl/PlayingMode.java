package com.github.syr0ws.fallenkingdom.game.model.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.ModeType;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayingMode implements Mode {

    private final TeamPlayer teamPlayer;
    private final GameModel game;

    public PlayingMode(TeamPlayer teamPlayer, GameModel game) {

        if(teamPlayer == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        this.teamPlayer = teamPlayer;
        this.game = game;
    }

    @Override
    public void set() {

        Player player = this.teamPlayer.getPlayer();
        GamePlayer gamePlayer = this.game.getGamePlayer(this.teamPlayer.getUUID());

        Team team = this.teamPlayer.getTeam();

        // Player is not playing yet. It will be after setting this mode.
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

        player.setPlayerListName(team.getDisplayName() + " " + player.getName());
    }

    @Override
    public void remove() {

    }

    @Override
    public ModeType getType() {
        return ModeType.PLAYING;
    }
}
