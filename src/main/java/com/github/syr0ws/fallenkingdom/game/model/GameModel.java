package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.tools.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface GameModel {

    void setPvPEnabled(boolean enabled);

    void setAssaultsEnabled(boolean enabled);

    void setState(GameState state);

    GameCycle getCycle();

    GameState getState();

    Location getSpawn();

    boolean isPvPEnabled();

    boolean areAssaultsEnabled();

    boolean isInsideEnemyBase(Player player);

    Optional<Team> getTeam(Player player);

    List<Team> getTeams();
}
