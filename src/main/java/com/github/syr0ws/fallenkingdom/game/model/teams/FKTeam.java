package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.universe.api.displays.Display;

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

    boolean hasBaseCaptured();

    boolean contains(UUID uuid);

    boolean contains(FKTeamPlayer player);

    int size();

    TeamColor getColor();

    FKTeamBase getBase();

    TeamState getState();

    Optional<? extends FKTeamPlayer> getTeamPlayer(UUID uuid);

    Collection<? extends FKTeamPlayer> getTeamPlayers();

    Collection<? extends FKTeamPlayer> getOnlineTeamPlayers();
}
