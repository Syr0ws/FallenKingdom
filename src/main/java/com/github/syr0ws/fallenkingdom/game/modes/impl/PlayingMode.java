package com.github.syr0ws.fallenkingdom.game.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.modes.Mode;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayingMode implements Mode {

    private final GameModel game;

    public PlayingMode(GameModel game) {
        this.game = game;
    }

    @Override
    public void setMode(Player player, boolean complete) {

        Optional<Team> optionalTeam = this.game.getTeam(player);

        if(!optionalTeam.isPresent())
            throw new UnsupportedOperationException("Cannot set playing mode to a player without team.");

        Team team = optionalTeam.get();

        if(complete) {

            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(team.getBase().getSpawn());
        }

        player.setPlayerListName(team.getDisplayName() + " " + player.getName());
    }

    @Override
    public void removeMode(Player player, boolean complete) {

        if(complete) {

            player.setExp(0);
            player.setLevel(0);
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
        }
    }
}
