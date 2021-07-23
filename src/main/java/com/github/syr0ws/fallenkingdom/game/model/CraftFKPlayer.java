package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.universe.commons.model.DefaultGamePlayer;
import com.github.syr0ws.universe.sdk.game.mode.ModeType;
import org.bukkit.entity.Player;

public class CraftFKPlayer extends DefaultGamePlayer implements FKPlayer {

    public CraftFKPlayer(Player player, ModeType type) {
        super(player, type);
    }
}
