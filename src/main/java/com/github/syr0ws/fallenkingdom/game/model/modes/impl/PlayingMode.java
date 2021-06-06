package com.github.syr0ws.fallenkingdom.game.model.modes.impl;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.chat.Chat;
import com.github.syr0ws.fallenkingdom.game.model.chat.GameChat;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.ModeType;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.scoreboards.Scoreboard;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.scoreboards.impl.GameBoard;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayingMode implements Mode {

    private final TeamPlayer teamPlayer;
    private final GameModel game;
    private final ScoreboardManager sbManager;
    private final FileConfiguration config;

    private final Chat gameChat;

    public PlayingMode(TeamPlayer teamPlayer, GameModel game, ScoreboardManager sbManager, FileConfiguration config) {

        if(teamPlayer == null)
            throw new IllegalArgumentException("GamePlayer cannot be null.");

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        this.teamPlayer = teamPlayer;
        this.game = game;
        this.sbManager = sbManager;
        this.config = config;

        this.gameChat = new GameChat(game, teamPlayer);
    }

    @Override
    public void set() {

        Player player = this.teamPlayer.getPlayer();
        GamePlayer gamePlayer = this.game.getGamePlayer(this.teamPlayer.getUUID());

        Team team = this.teamPlayer.getTeam();

        // Player is not playing yet. It will be after setting this mode.
        if(!gamePlayer.isPlaying()) {

            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setBedSpawnLocation(null);
            player.teleport(team.getBase().getSpawn());
        }

        player.setPlayerListName(team.getDisplayName() + " " + player.getName());

        Scoreboard scoreboard = new GameBoard(this.game, this.teamPlayer, this.config);
        this.sbManager.addScoreboard(player, scoreboard);
    }

    @Override
    public void remove() {

        this.sbManager.removeScoreboard(this.teamPlayer.getPlayer());
    }

    @Override
    public Chat getChat() {
        return this.gameChat;
    }

    @Override
    public ModeType getType() {
        return ModeType.PLAYING;
    }
}
