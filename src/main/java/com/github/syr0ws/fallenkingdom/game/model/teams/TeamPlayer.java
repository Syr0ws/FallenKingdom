package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.GamePlayer;

public class TeamPlayer {

    private final GamePlayer player;
    private final Team team;
    private int kills, deaths;
    private boolean alive;

    public TeamPlayer(GamePlayer player, Team team) {
        this.player = player;
        this.team = team;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void addKills() {
        this.kills++;
    }

    public void addDeaths() {
        this.deaths++;
    }

    public double getKDR() {
        return this.deaths == 0 ? this.kills : (double) this.kills / this.deaths;
    }

    public boolean isEliminated() {
        return this.alive;
    }

    public GamePlayer getGamePlayer() {
        return this.player;
    }

    public Team getTeam() {
        return this.team;
    }
}
