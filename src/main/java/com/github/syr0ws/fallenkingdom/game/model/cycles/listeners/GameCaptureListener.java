package com.github.syr0ws.fallenkingdom.game.model.cycles.listeners;

import com.github.syr0ws.fallenkingdom.events.GamePlayerLeaveEvent;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class GameCaptureListener implements Listener {

    private final GameController controller;
    private final GameModel game;

    public GameCaptureListener(GameController controller) {
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

            if(!this.game.isCapturing(teamPlayer)) this.onCaptureStart(teamPlayer, optionalTeamTo.get());
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
}
