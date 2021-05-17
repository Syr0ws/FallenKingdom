package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.game.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private final String name;
    private Mode gameMode, current;

    public GamePlayer(Player player, Mode mode) {

        this.uuid = player.getUniqueId();
        this.name = player.getName();

        this.setCurrentMode(mode);
        this.setGameMode(mode);
    }

    public void setGameMode(Mode gameMode) {

        if(gameMode == null)
            throw new IllegalArgumentException("Game mode cannot be null.");

        this.gameMode = gameMode;
    }

    public void setCurrentMode(Mode current) {

        if(gameMode == null)
            throw new IllegalArgumentException("Current mode cannot be null.");

        this.current = current;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Mode getGameMode() {
        return this.gameMode;
    }

    public Mode getCurrentMode() {
        return this.current;
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
