package com.github.syr0ws.fallenkingdom.game.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.modes.Mode;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class WaitingMode implements Mode {

    private final GameModel game;

    public WaitingMode(GameModel game) {
        this.game = game;
    }

    @Override
    public void setMode(Player player, boolean complete) {

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(this.game.getSpawn());
    }

    @Override
    public void removeMode(Player player, boolean complete) {

        player.setExp(0);
        player.setLevel(0);
        player.getInventory().clear();
    }
}
