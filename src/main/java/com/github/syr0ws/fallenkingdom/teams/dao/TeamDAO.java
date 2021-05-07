package com.github.syr0ws.fallenkingdom.teams.dao;

import com.github.syr0ws.fallenkingdom.teams.Team;

import java.util.Collection;

public interface TeamDAO {

    Collection<Team> loadTeams();
}
