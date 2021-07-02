package com.github.syr0ws.fallenkingdom.game.model.v2.teams;

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
