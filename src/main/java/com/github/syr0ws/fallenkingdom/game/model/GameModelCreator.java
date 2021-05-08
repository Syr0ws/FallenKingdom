package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.game.notifiers.PvPNotifier;
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

public class GameModelCreator {

    private final Plugin plugin;
    private final TeamDAO teamDAO;

    public GameModelCreator(Plugin plugin) {
        this.plugin = plugin;
        this.teamDAO = new ConfigTeamDAO(plugin);
    }

    public GameModel getModel() {

        Location spawn = this.loadSpawn();
        List<Team> teams = this.loadTeams();

        Game game = new Game(this.plugin, spawn, teams);

        this.addNotifiers(game);

        return game;
    }

    private void addNotifiers(Game game) {
        game.addObserver(new PvPNotifier(game, this.plugin));
        game.addObserver(new AssaultsNotifier(game, this.plugin));
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
}
