package com.github.syr0ws.fallenkingdom.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPlayer {

    private final String name;
    private final UUID uuid;
    private int kills, deaths;

    public TeamPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public TeamPlayer(Player player) {
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
}
