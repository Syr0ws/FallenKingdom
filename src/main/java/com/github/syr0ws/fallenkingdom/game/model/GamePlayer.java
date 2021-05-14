package com.github.syr0ws.fallenkingdom.game.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private final String name;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public boolean isPlayer(Player player) {
        return player.getUniqueId().equals(this.uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
