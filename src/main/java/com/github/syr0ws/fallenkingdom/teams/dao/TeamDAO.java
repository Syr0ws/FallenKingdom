package com.github.syr0ws.fallenkingdom.teams.dao;

import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.teams.TeamException;

import java.util.Collection;

public interface TeamDAO {

    Collection<Team> loadTeams() throws TeamException;
}
