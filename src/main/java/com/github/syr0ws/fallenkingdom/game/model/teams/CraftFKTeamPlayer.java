package com.github.syr0ws.fallenkingdom.game.model.teams;

import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CraftFKTeamPlayer implements FKTeamPlayer {

    private final FKTeam team;
    private final FKPlayer player;

    private boolean alive;
    private int kills, deaths;

    public CraftFKTeamPlayer(FKTeam team, FKPlayer player) {

        if(team == null)
            throw new IllegalArgumentException("FKTeam cannot be null.");

        if(player == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        this.team = team;
        this.player = player;
        this.alive = true;
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
    public Player getPlayer() {
        return this.player.getPlayer();
    }

    @Override
    public FKTeam getTeam() {
        return this.team;
    }

    @Override
    public boolean isOnline() {
        return this.player.isOnline();
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

    @Override
    public FKPlayer getFKPlayer() {
        return this.player;
    }
}
