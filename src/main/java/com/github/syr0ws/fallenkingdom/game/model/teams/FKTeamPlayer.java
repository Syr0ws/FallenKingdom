package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FKTeamPlayer implements TeamPlayer {

    private final Team team;
    private final GamePlayer player;

    private boolean alive;
    private int kills, deaths;

    public FKTeamPlayer(Team team, GamePlayer player) {

        if(team == null)
            throw new IllegalArgumentException("Team cannot be null.");

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        this.team = team;
        this.player = player;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public UUID getUUID() {
        return this.player.getUUID();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public boolean isOnline() {
        return this.player.isOnline();
    }

    @Override
    public Player getPlayer() {
        return this.player.getPlayer();
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public void addKill() {
        this.kills++;
    }

    @Override
    public void addDeath() {
        this.deaths++;
    }

    @Override
    public int getKills() {
        return this.kills;
    }

    @Override
    public int getDeaths() {
        return this.deaths;
    }

    @Override
    public double getKDR() {
        return this.deaths == 0 ? this.kills : (double) this.kills / this.deaths;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }
}
