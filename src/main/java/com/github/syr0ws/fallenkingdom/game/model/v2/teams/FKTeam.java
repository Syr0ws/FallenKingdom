package com.github.syr0ws.fallenkingdom.game.model.v2.teams;

import com.github.syr0ws.universe.displays.Display;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface FKTeam {

    void sendDisplay(Display display);

    void sendDisplay(Display display, Predicate<FKTeamPlayer> predicate);

    String getName();

    String getDisplayName();

    boolean isEliminated();

    boolean contains(UUID uuid);

    boolean contains(FKTeamPlayer player);

    int size();

    TeamColor getColor();

    TeamBase getBase();

    TeamState getState();

    Optional<? extends FKTeamPlayer> getTeamPlayer(UUID uuid);

    Collection<? extends FKTeamPlayer> getTeamPlayers();

    Collection<? extends FKTeamPlayer> getOnlineTeamPlayers();
}
