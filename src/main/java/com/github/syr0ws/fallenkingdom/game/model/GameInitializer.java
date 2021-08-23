package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.model.settings.CraftFKSettings;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.CraftFKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.universe.api.game.model.GameException;
import com.github.syr0ws.universe.api.settings.SettingManager;
import com.github.syr0ws.universe.sdk.game.settings.manager.CacheSettingManager;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameInitializer {

    private final Plugin plugin;

    public GameInitializer(Plugin plugin) {
        this.plugin = plugin;
    }

    public CraftFKModel getGame() throws GameException {

        // Loading game settings.
        FKSettings accessor = this.loadSettings();

        // Loading teams.
        List<CraftFKTeam> teams = this.loadTeams(accessor);

        return new CraftFKModel(accessor, teams);
    }

    private List<CraftFKTeam> loadTeams(FKSettings accessor) throws GameException {

        ConfigTeamDAO dao = new ConfigTeamDAO(this.plugin, accessor);

        List<CraftFKTeam> teams = new ArrayList<>();

        try {

            Collection<CraftFKTeam> collection = dao.loadTeams();
            teams.addAll(collection);

        } catch (TeamException e) { e.printStackTrace(); }

        if(teams.size() <= 2)
            throw new GameException("Number of teams cannot be lower than 2.");

        return teams;
    }

    private FKSettings loadSettings() {

        SettingManager manager = new CacheSettingManager();

        CraftFKSettings accessor = new CraftFKSettings(manager);
        accessor.init(this.plugin.getConfig());

        return accessor;
    }
}
