package com.github.syr0ws.fallenkingdom.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamParticipant {

    private final String name;
    private final UUID uuid;

    public TeamParticipant(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public TeamParticipant(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
