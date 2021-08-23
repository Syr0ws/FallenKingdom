package com.github.syr0ws.fallenkingdom.api.model.teams;

import java.util.Collection;

public interface TeamDAO<T extends FKTeam> {

    Collection<T> loadTeams() throws TeamException;
}
