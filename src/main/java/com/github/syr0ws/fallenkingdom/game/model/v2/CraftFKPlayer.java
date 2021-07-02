package com.github.syr0ws.fallenkingdom.game.model.v2;

import com.github.syr0ws.universe.game.model.mode.ModeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CraftFKPlayer implements FKPlayer {

    private final UUID uuid;
    private final String name;

    private boolean playing;
    private ModeType modeType;

    public CraftFKPlayer(Player player) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public void setPlaying() {
        this.playing = true;
    }

    public void setModeType(ModeType type) {

        if(type == null)
            throw new IllegalArgumentException("ModeType cannot be null.");

        this.modeType = type;
    }

    @Override
    public ModeType getModeType() {
        return this.modeType;
    }

    @Override
    public boolean isPlaying() {
        return this.playing;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(this.uuid) != null;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
