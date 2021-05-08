package com.github.syr0ws.fallenkingdom.game.controller;

import com.github.syr0ws.fallenkingdom.game.model.Game;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.teams.TeamException;
import com.github.syr0ws.fallenkingdom.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.fallenkingdom.teams.dao.TeamDAO;
import com.github.syr0ws.fallenkingdom.tools.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleGameController implements GameController {

    private final Plugin plugin;
    private final TeamDAO teamDAO;
    private Game game;

    public SimpleGameController(Plugin plugin) {
        this.plugin = plugin;
        this.teamDAO = new ConfigTeamDAO(plugin);
    }

    // TODO Should controller init the model ?
    public void init() {

        Location spawn = this.loadSpawn();
        List<Team> teams = this.loadTeams();

        this.game = new Game(this.plugin, spawn, teams);
    }

    private List<Team> loadTeams() {

        List<Team> teams = new ArrayList<>();

        try {

            Collection<Team> collection = this.teamDAO.loadTeams();
            teams.addAll(collection);

        } catch (TeamException e) { e.printStackTrace(); }

        return teams;
    }

    private Location loadSpawn() {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("spawn");

        return new Location(section);
    }

    @Override
    public void startGame() {
        this.game.setState(GameState.STARTING);
    }

    @Override
    public void stopGame() {
        this.game.setState(GameState.FINISHED);
    }

    @Override
    public GameModel getModel() {
        return this.game;
    }
}
