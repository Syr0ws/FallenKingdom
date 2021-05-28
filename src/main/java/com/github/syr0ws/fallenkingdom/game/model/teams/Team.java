package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface Team {

    void sendDisplay(Display display);

    void sendDisplay(Display display, Predicate<TeamPlayer> predicate);

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

    Collection<? extends TeamPlayer> getOnlineTeamPlayers();

    Collection<? extends Player> getOnlinePlayers();

    Collection<? extends TeamPlayer> getTeamPlayers();
}
