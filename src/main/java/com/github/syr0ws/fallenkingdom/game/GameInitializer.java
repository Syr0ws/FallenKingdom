package com.github.syr0ws.fallenkingdom.game;

import com.github.syr0ws.fallenkingdom.game.model.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.settings.GameSettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.ConfigTeamDAO;
import com.github.syr0ws.fallenkingdom.game.model.teams.dao.TeamDAO;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
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

        SettingAccessor accessor = this.loadSettings();

        return new FKGame(accessor, teams);
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

    private SettingAccessor loadSettings() {

        GameSettingAccessor accessor = new GameSettingAccessor();
        accessor.init(this.plugin.getConfig());

        return accessor;
    }
}
