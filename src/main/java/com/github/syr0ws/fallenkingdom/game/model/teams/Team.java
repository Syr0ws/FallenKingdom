package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Team {

    String getName();

    String getDisplayName();

    boolean isEliminated();

    boolean contains(UUID uuid);

    boolean contains(GamePlayer player);

    boolean contains(TeamPlayer player);

    int size();

    TeamColor getColor();

    TeamBase getBase();

    TeamState getState();

    Optional<TeamPlayer> getTeamPlayer(UUID uuid);

    Optional<TeamPlayer> getTeamPlayer(GamePlayer player);

    List<? extends TeamPlayer> getPlayers();
}
