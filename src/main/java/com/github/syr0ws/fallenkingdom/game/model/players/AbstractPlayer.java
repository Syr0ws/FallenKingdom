package com.github.syr0ws.fallenkingdom.game.model.players;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface AbstractPlayer {

    UUID getUUID();

    String getName();

    boolean isOnline();

    Player getPlayer();
}
