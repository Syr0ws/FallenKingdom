package com.github.syr0ws.fallenkingdom.game.model.modes;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.game.model.mode.ModeType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class WaitingMode extends FKMode {

    private final FKModel model;

    public WaitingMode(FKGame game) {
        super(game);

        this.model = game.getGameModel();
    }

    @Override
    public void enable(Player player) {

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(this.model.getSpawn());
    }

    @Override
    public void disable(Player player) {

    }

    @Override
    public ModeType getType() {
        return DefaultModeType.WAITING;
    }
}
