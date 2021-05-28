package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.game.model.capture.Capture;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface GameModel extends AttributeObservable {

    TeamPlayer setTeam(GamePlayer player, Team team);

    TeamPlayer removeTeam(GamePlayer player);

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    void setGamePlayerMode(UUID uuid, Mode mode);

    void addGamePlayer(Player player, Mode mode);

    void removeGamePlayer(Player player);

    void addTime();

    boolean isStarted();

    boolean isFinished();

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    boolean isValid(Team team);

    boolean isValid(TeamPlayer player);

    boolean isValid(Capture capture);

    boolean hasTeam(UUID uuid);

    boolean hasTeam(GamePlayer player);

    boolean isTeamPlayer(UUID uuid);

    boolean isTeamPlayer(GamePlayer player);

    boolean isCaptured(Team team);

    boolean isCapturing(TeamPlayer player);

    int getTime();

    GameCycle getCycle();

    GameState getState();

    Location getSpawn();

    SettingManager getSettings();

    GamePlayer getGamePlayer(UUID uuid);

    Optional<? extends GamePlayer> getGamePlayer(String name);

    Optional<? extends TeamPlayer> getTeamPlayer(UUID uuid);

    Optional<? extends TeamPlayer> getTeamPlayer(GamePlayer player);

    Optional<? extends Team> getTeam(UUID uuid);

    Optional<? extends Team> getTeam(GamePlayer player);

    Optional<? extends Team> getTeamByName(String name);

    Optional<? extends Capture> getCapture(Team captured);

    Collection<? extends Capture> getCaptures();

    Collection<? extends Team> getTeams();

    Collection<? extends TeamPlayer> getTeamPlayers();

    Collection<? extends GamePlayer> getPlayers();

    Collection<? extends GamePlayer> getOnlinePlayers();
}
