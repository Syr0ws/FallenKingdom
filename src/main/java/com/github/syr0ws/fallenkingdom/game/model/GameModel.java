package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public interface GameModel extends AttributeObservable {

    void join(GamePlayer player);

    void leave(GamePlayer player);

    TeamPlayer setTeam(GamePlayer player, Team team);

    Team removeTeam(TeamPlayer player);

    void setCycle(GameCycle cycle);

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    void addTime();

    GameCycle getCycle();

    GameState getState();

    Location getSpawn();

    boolean isStarted();

    boolean isFinished();

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    int getTime();

    int countTeams();

    boolean hasTeam(Player player);

    SettingManager getSettingManager();

    GamePlayer getGamePlayer(Player player);

    Optional<TeamPlayer> getTeamPlayer(Player player);

    Optional<TeamPlayer> getTeamPlayer(GamePlayer player);

    Optional<Team> getTeam(Player player);

    Optional<Team> getTeam(String name);

    Collection<GamePlayer> getGamePlayers();

    Collection<TeamPlayer> getTeamPlayers();

    Collection<Team> getTeams();
}
