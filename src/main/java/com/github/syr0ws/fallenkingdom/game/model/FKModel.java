package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.sdk.game.model.GameModel;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface FKModel extends GameModel {

    FKTeamPlayer setTeam(FKPlayer player, FKTeam team);

    FKTeamPlayer removeTeam(FKPlayer player);

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    FKSettings getSettings();

    boolean isValid(FKTeam team);

    boolean isValid(FKTeamPlayer player);

    boolean hasTeam(UUID uuid);

    boolean hasTeam(FKPlayer player);

    boolean isTeamPlayer(UUID uuid);

    boolean isTeamPlayer(FKPlayer player);

    FKPlayer getFKPlayer(UUID uuid);

    Optional<? extends FKTeamPlayer> getTeamPlayer(UUID uuid);

    Optional<? extends FKTeam> getTeam(UUID uuid);

    Optional<? extends FKTeam> getTeamByName(String name);

    Collection<? extends FKTeam> getTeams();

    Collection<? extends FKTeamPlayer> getTeamPlayers();
}
