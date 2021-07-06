package com.github.syr0ws.fallenkingdom.game.model.teams.dao;

import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;

import java.util.Collection;

public interface TeamDAO<T extends FKTeam> {

    Collection<T> loadTeams() throws TeamException;
}
