package com.github.syr0ws.fallenkingdom.game.model.v2.teams.dao;

import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.TeamException;

import java.util.Collection;

public interface TeamDAO<T extends FKTeam> {

    Collection<T> loadTeams() throws TeamException;
}
