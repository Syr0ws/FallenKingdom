package com.github.syr0ws.fallenkingdom.api.model.teams;

import com.github.syr0ws.fallenkingdom.api.model.FKPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface FKTeamPlayer {

    UUID getUUID();

    String getName();

    Player getPlayer();

    FKTeam getTeam();

    boolean isOnline();

    void addKill();

    void addDeath();

    int getKills();

    int getDeaths();

    double getKDR();

    boolean isAlive();

    FKPlayer getFKPlayer();
}
