package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Comparator;

public class SimpleGameController implements GameController {

    private final Plugin plugin;
    private final GameModel model;

    public SimpleGameController(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public void startGame() throws GameException {

        if(this.model.isStarted())
            throw new GameException("A game is already started.");

        if(this.model.countTeams() < 2)
            throw new GameException("Cannot start a game with less than 2 teams.");

        if(Bukkit.getOnlinePlayers().size() < 1) // TODO to change
            throw new GameException("Cannot start a game with less than 2 players.");

        // Setting all players without team in a team.
        this.setPlayersInTeam();

        this.model.setState(GameState.STARTING);
    }

    @Override
    public void stopGame() throws GameException {

        if(!this.model.isStarted())
            throw new GameException("No game is started.");

        GameState current = this.model.getState();
        GameState state = current == GameState.STARTING ? GameState.WAITING : GameState.FINISHED;

        this.model.setState(state);
    }

    private void setPlayersInTeam() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            // If the player is already in a team, nothing to do.
            if(this.model.hasTeam(player)) continue;

            // Selecting the team with the minimum of players to equilibrate.
            // There is always an available team at this point.
            Team team = this.model.getTeams().stream().min(Comparator.comparingInt(Team::size)).get();
            team.addPlayer(player);
        }
    }
}
