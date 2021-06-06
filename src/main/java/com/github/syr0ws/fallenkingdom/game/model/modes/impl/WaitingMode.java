package com.github.syr0ws.fallenkingdom.game.model.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.chat.Chat;
import com.github.syr0ws.fallenkingdom.game.model.chat.WaitingChat;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.ModeType;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.Scoreboard;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.scoreboards.impl.WaitingBoard;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WaitingMode implements Mode {

    private final GamePlayer gamePlayer;
    private final GameModel game;

    private final FileConfiguration config;
    private final ScoreboardManager sbManager;

    private final Chat chat;

    public WaitingMode(GamePlayer gamePlayer, GameModel game, FileConfiguration config, ScoreboardManager sbManager) {

        if(gamePlayer == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        this.gamePlayer = gamePlayer;
        this.game = game;

        this.config = config;
        this.sbManager = sbManager;

        this.chat = new WaitingChat(game);
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

        Scoreboard scoreboard = new WaitingBoard(this.game, player, this.config);
        this.sbManager.addScoreboard(player, scoreboard);
    }

    @Override
    public void remove() {

        Player player = this.gamePlayer.getPlayer();

        this.sbManager.removeScoreboard(player);
    }

    @Override
    public Chat getChat() {
        return this.chat;
    }

    @Override
    public ModeType getType() {
        return ModeType.WAITING;
    }
}
