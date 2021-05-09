package com.github.syr0ws.fallenkingdom.game.model.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPlayer {

    private final Team team;
    private final String name;
    private final UUID uuid;
    private int kills, deaths;

    public TeamPlayer(Team team, String name, UUID uuid) {
        this.team = team;
        this.name = name;
        this.uuid = uuid;
    }

    public TeamPlayer(Team team, Player player) {
        this.team = team;
        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public boolean is(Player player) {
        return player.getUniqueId().equals(this.uuid);
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public Team getTeam() {
        return this.team;
    }
}
