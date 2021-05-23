package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.players.AbstractPlayer;

public interface TeamPlayer extends AbstractPlayer {

    Team getTeam();

    void addKill();

    void addDeath();

    int getKills();

    int getDeaths();

    double getKDR();

    boolean isAlive();
}
