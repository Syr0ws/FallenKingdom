package com.github.syr0ws.fallenkingdom.plugin.game.model;

import com.github.syr0ws.fallenkingdom.api.model.FKPlayer;
import com.github.syr0ws.universe.sdk.game.model.DefaultGamePlayer;
import org.bukkit.entity.Player;

public class CraftFKPlayer extends DefaultGamePlayer implements FKPlayer {

    public CraftFKPlayer(Player player) {
        super(player);
    }
}
