package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.tools.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public interface GameModel extends AttributeObservable {

    void setTeam(Player player, Team team);

    void removeTeam(Player player);

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    void setState(GameState state);

    GameCycle getCycle();

    GameState getState();

    Location getSpawn();

    boolean isStarted();

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    boolean isInsideEnemyBase(Player player);

    boolean isInsideBase(Location location);

    boolean hasTeam(Player player);

    int countTeams();

    int countPlayers();

    Optional<Team> getTeam(Player player);

    Optional<Team> getTeamByName(String name);

    Collection<TeamPlayer> getPlayers();

    Collection<Team> getTeams();
}
