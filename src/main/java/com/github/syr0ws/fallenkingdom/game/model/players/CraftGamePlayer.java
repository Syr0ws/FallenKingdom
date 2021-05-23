package com.github.syr0ws.fallenkingdom.game.model.players;

import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.ModeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CraftGamePlayer implements GamePlayer {

    private final UUID uuid;
    private final String name;
    private Mode mode;

    public CraftGamePlayer(Player player) {

        if(player == null)
            throw new IllegalArgumentException("Player cannot be null.");

        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public void setMode(Mode mode) {

        if(mode == null)
            throw new IllegalArgumentException("Mode cannot be null.");

        if(this.mode != null) this.mode.remove(); // Removing old mode.

        mode.set(); // Setting new mode.
        this.mode = mode;
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public boolean isPlaying() {
        return this.mode.getType() == ModeType.PLAYING;
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
