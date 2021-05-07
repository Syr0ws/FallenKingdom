package com.github.syr0ws.fallenkingdom.teams.dao;

import com.github.syr0ws.fallenkingdom.teams.Team;

import java.util.Collection;

public class ConfigTeamDAO implements TeamDAO {

    private static final String TEAM_FILE_NAME = "teams.yml";

    @Override
    public Collection<Team> loadTeams() {
        return null;
    }
}
