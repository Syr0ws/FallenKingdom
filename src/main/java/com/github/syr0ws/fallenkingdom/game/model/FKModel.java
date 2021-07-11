package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.game.model.GameModel;
import com.github.syr0ws.universe.game.model.GamePlayer;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface FKModel extends GameModel {

    FKTeamPlayer setTeam(GamePlayer player, FKTeam team);

    FKTeamPlayer removeTeam(GamePlayer player);

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    void addTime();

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    int getTime();

    Location getSpawn();

    SettingAccessor getSettings();

    GameState getState();

    boolean isValid(FKTeam team);

    boolean isValid(FKTeamPlayer player);

    boolean hasTeam(UUID uuid);

    boolean hasTeam(GamePlayer player);

    boolean isTeamPlayer(UUID uuid);

    boolean isTeamPlayer(GamePlayer player);

    @Override
    FKPlayer getPlayer(UUID uuid);

    @Override
    Optional<? extends FKPlayer> getPlayer(String s);

    @Override
    Collection<? extends FKPlayer> getOnlinePlayers();

    @Override
    Collection<? extends FKPlayer> getPlayers();

    Optional<? extends FKTeamPlayer> getTeamPlayer(UUID uuid);

    Optional<? extends FKTeam> getTeam(UUID uuid);

    Optional<? extends FKTeam> getTeamByName(String name);

    Collection<? extends FKTeam> getTeams();

    Collection<? extends FKTeamPlayer> getTeamPlayers();
}
