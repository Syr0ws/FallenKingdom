package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.game.model.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.TeamDAO;
import com.github.syr0ws.fallenkingdom.settings.dao.FKSettingDAO;
import com.github.syr0ws.fallenkingdom.settings.dao.SettingDAO;
import com.github.syr0ws.fallenkingdom.settings.manager.ConfigSettingManager;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GameInitializer {

    private final Plugin plugin;

    public GameInitializer(Plugin plugin) {
        this.plugin = plugin;
    }

    public FKGame getGame() throws GameException {

        List<FKTeam> teams = this.loadTeams();

        if(teams.size() <= 2)
            throw new GameException("Number of teams cannot be lower than 2.");

        SettingManager settingManager = this.loadSettings();

        return new FKGame(settingManager, teams);
    }

    private List<FKTeam> loadTeams() {

        TeamDAO<FKTeam> dao = new ConfigTeamDAO(this.plugin);

        List<FKTeam> teams = new ArrayList<>();

        try {

            Collection<FKTeam> collection = dao.loadTeams();
            teams.addAll(collection);

        } catch (TeamException e) { e.printStackTrace(); }

        return teams;
    }

    private SettingManager loadSettings() {

        SettingDAO dao = new FKSettingDAO(this.plugin);
        SettingManager manager = new ConfigSettingManager();

        List<GameSettings> settings = Arrays.asList(GameSettings.values());
        settings.forEach(setting -> manager.addSetting(setting, setting.getSetting()));

        dao.readSettings("general-settings", manager.getSettings());
        dao.readSettings("game-settings", manager.getSettings());

        return manager;
    }
}
