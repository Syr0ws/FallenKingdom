package com.github.syr0ws.fallenkingdom.game.model.teams.dao;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;

import java.util.Collection;

public interface TeamDAO<T extends Team> {

    Collection<T> loadTeams() throws TeamException;
}
