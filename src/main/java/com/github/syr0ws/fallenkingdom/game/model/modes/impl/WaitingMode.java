package com.github.syr0ws.fallenkingdom.game.model.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.ModeType;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class WaitingMode implements Mode {

    private final GamePlayer gamePlayer;
    private final GameModel game;

    public WaitingMode(GamePlayer gamePlayer, GameModel game) {

        if(gamePlayer == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public void set() {

        Player player = this.gamePlayer.getPlayer();

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(this.game.getSpawn());
    }

    @Override
    public void remove() {

    }

    @Override
    public ModeType getType() {
        return ModeType.WAITING;
    }
}
