package com.github.syr0ws.fallenkingdom.game.model.cycles.listeners;

import com.github.syr0ws.fallenkingdom.events.*;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.placholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.universe.displays.impl.Message;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class GameCaptureListener implements Listener {

    private final Plugin plugin;
    private final GameController controller;
    private final GameModel game;

    public GameCaptureListener(Plugin plugin, GameController controller) {
        this.plugin = plugin;
        this.controller = controller;
        this.game = controller.getGame();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location from = event.getFrom(), to = event.getTo();

        // This allows less calculations.
        if(from.getBlock().equals(to.getBlock())) return;

        // A vault can be captured only when assaults are enabled.
        if(!this.game.areAssaultsEnabled()) return;

        Optional<? extends TeamPlayer> optional = this.game.getTeamPlayer(player.getUniqueId());

        // Checking if the player is playing (is a TeamPlayer).
        if(!optional.isPresent()) return;

        TeamPlayer teamPlayer = optional.get();

        // Checking if the player is alive.
        if(!teamPlayer.isAlive()) return;

        Optional<? extends Team> optionalTeamFrom = this.getEnemyVault(teamPlayer, from);
        Optional<? extends Team> optionalTeamTo = this.getEnemyVault(teamPlayer, to);

        // First, checking if the player is leaving a vault.
        // If he's not, checking if the player is entering in a vault.
        if(optionalTeamFrom.isPresent() && !optionalTeamTo.isPresent()) {

            if(this.game.isCapturing(teamPlayer)) this.onCaptureStop(teamPlayer);

        } else if(optionalTeamTo.isPresent() && !optionalTeamFrom.isPresent()) {

            // If the player is already capturing, do not do anything.
            if(this.game.isCapturing(teamPlayer)) return;

            Team team = optionalTeamTo.get();

            // If the team is already eliminated, do no do anything.
            if(team.isEliminated()) return;

            this.onCaptureStart(teamPlayer, optionalTeamTo.get());
        }
    }

    @EventHandler
    public void onGamePlayerLeave(GamePlayerLeaveEvent event) {

        GamePlayer player = event.getPlayer();

        Optional<? extends TeamPlayer> optionalPlayer = this.game.getTeamPlayer(player.getUUID());

        // Player is not a TeamPlayer (is not playing).
        if(!optionalPlayer.isPresent()) return;

        TeamPlayer teamPlayer = optionalPlayer.get();

        // Checking that the player is capturing a base.
        if(!this.game.isCapturing(teamPlayer)) return;

        // If the player is capturing a base, stopping the capture.
        this.onCaptureStop(teamPlayer);
    }

    @EventHandler
    public void onPlayerCaptureBaseStart(PlayerCaptureBaseStartEvent event) {

        GamePlayer gamePlayer = event.getPlayer();

        Message message = new Message(this.getCaptureSection().getString("player-start-capture"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCapturedTeam().getDisplayName());

        message.displayTo(gamePlayer.getPlayer());
    }

    @EventHandler
    public void onPlayerCaptureBaseStop(PlayerCaptureBaseStopEvent event) {

        GamePlayer gamePlayer = event.getPlayer();

        Message message = new Message(this.getCaptureSection().getString("player-stop-capture"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCapturedTeam().getDisplayName());

        message.displayTo(gamePlayer.getPlayer());
    }

    @EventHandler
    public void onTeamBaseCaptureStart(TeamBaseCaptureStartEvent event) {

        Message message = new Message(this.getCaptureSection().getString("team-base-start-capture"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCatcher().getDisplayName());

        event.getTeam().sendDisplay(message, TeamPlayer::isAlive);
    }

    @EventHandler
    public void onTeamBaseCaptureStop(TeamBaseCaptureStopEvent event) {

        Message message = new Message(this.getCaptureSection().getString("team-base-stop-capture"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCatcher().getDisplayName());

        event.getTeam().sendDisplay(message, TeamPlayer::isAlive);
    }

    private void onCaptureStop(TeamPlayer player) {

        try { this.controller.stopCapture(player);
        } catch (GameException e) { e.printStackTrace(); }
    }

    private void onCaptureStart(TeamPlayer player, Team team) {

        try { this.controller.startCapture(player, team);
        } catch (GameException e) { e.printStackTrace(); }
    }

    private Optional<? extends Team> getEnemyVault(TeamPlayer player, Location location) {
        return this.game.getTeams().stream()
                .filter(team -> !team.contains(player))
                .filter(team -> team.getBase().getVault().isIn(location))
                .findFirst();
    }

    private ConfigurationSection getCaptureSection() {
        return this.plugin.getConfig().getConfigurationSection("capture");
    }
}
