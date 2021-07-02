package com.github.syr0ws.fallenkingdom.game.model.v2;

import com.github.syr0ws.fallenkingdom.game.model.v2.settings.GameSettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.v2.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.CraftFKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.TeamException;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.universe.game.model.GameException;
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
        SettingAccessor accessor = this.loadSettings();

        // Loading teams.
        List<CraftFKTeam> teams = this.loadTeams();

        return new CraftFKModel(accessor, teams);
    }

    private List<CraftFKTeam> loadTeams() throws GameException {

        ConfigTeamDAO dao = new ConfigTeamDAO(this.plugin);

        List<CraftFKTeam> teams = new ArrayList<>();

        try {

            Collection<CraftFKTeam> collection = dao.loadTeams();
            teams.addAll(collection);

        } catch (TeamException e) { e.printStackTrace(); }

        if(teams.size() <= 2)
            throw new GameException("Number of teams cannot be lower than 2.");

        return teams;
    }

    private SettingAccessor loadSettings() {

        GameSettingAccessor accessor = new GameSettingAccessor();
        accessor.init(this.plugin.getConfig());

        return accessor;
    }
}
