package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.universe.game.model.GamePlayer;

public interface FKTeamPlayer extends GamePlayer {

    FKTeam getTeam();

    void addKill();

    void addDeath();

    int getKills();

    int getDeaths();

    double getKDR();

    boolean isAlive();
}
