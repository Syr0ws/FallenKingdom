package com.github.syr0ws.fallenkingdom.game.modes;

import org.bukkit.entity.Player;

public interface Mode {

    void setMode(Player player, boolean complete);

    void removeMode(Player player, boolean complete);
}
