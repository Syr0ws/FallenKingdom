package com.github.syr0ws.fallenkingdom.capture.area.manager;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.capture.area.listeners.AreaCaptureListener;
import com.github.syr0ws.fallenkingdom.capture.area.model.CraftAreaCaptureModel;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.v2.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.listeners.ListenerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class CraftAreaCaptureManager implements CaptureManager {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    private final CraftAreaCaptureModel captureModel;
    private final ListenerManager listenerManager;

    public CraftAreaCaptureManager(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
        this.controller = game.getGameController();

        this.captureModel = new CraftAreaCaptureModel();
        this.listenerManager = new ListenerManager(game);
    }

    @Override
    public void enable() {
        this.listenerManager.addListener(new CaptureListener());
        this.listenerManager.addListener(new AreaCaptureListener(this.game));
    }

    @Override
    public void disable() {
        this.listenerManager.removeListeners();
    }

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.AREA;
    }

    private void onCaptureStart(FKTeam captured, FKTeamPlayer player) {

    }

    private void onCaptureStop(FKTeamPlayer player) {

    }

    private class CaptureListener implements Listener {

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {

            Player player = event.getPlayer();
            Location from = event.getFrom(), to = event.getTo();

            // This allows less calculations.
            if(from.getBlock().equals(to.getBlock())) return;

            this.handleMove(player, from, to);
        }

        @EventHandler
        public void onPlayerTeleport(PlayerTeleportEvent event) {

            Player player = event.getPlayer();
            Location from = event.getFrom(), to = event.getTo();

            this.handleMove(player, from, to);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            Optional<? extends FKTeamPlayer> optionalPlayer = model.getTeamPlayer(player.getUniqueId());

            // Player is not a FKTeamPlayer (is not playing).
            if(!optionalPlayer.isPresent()) return;

            FKTeamPlayer teamPlayer = optionalPlayer.get();

            // Checking that the player is capturing a base.
            if(!captureModel.isCapturing(teamPlayer)) return;

            // If the player is capturing a base, stopping the capture.
            onCaptureStop(teamPlayer);
        }

        private void handleMove(Player player, Location from, Location to) {

            // A vault can be captured only when assaults are enabled.
            if(!model.areAssaultsEnabled()) return;

            Optional<? extends FKTeamPlayer> optional = model.getTeamPlayer(player.getUniqueId());

            // Checking if the player is playing (is a FKTeamPlayer).
            if(!optional.isPresent()) return;

            FKTeamPlayer teamPlayer = optional.get();

            // Checking if the player is alive.
            if(!teamPlayer.isAlive()) return;

            // Checking if the player is already capturing.
            if(captureModel.isCapturing(teamPlayer)) return;

            Optional<? extends FKTeam> optionalTeamFrom = this.getEnemyVault(teamPlayer, from);
            Optional<? extends FKTeam> optionalTeamTo = this.getEnemyVault(teamPlayer, to);

            // First, checking if the player is leaving a vault.
            // If he's not, checking if the player is entering in a vault.
            if(optionalTeamFrom.isPresent() && !optionalTeamTo.isPresent()) {

                if(captureModel.isCapturing(teamPlayer)) onCaptureStop(teamPlayer);

            } else if(optionalTeamTo.isPresent() && !optionalTeamFrom.isPresent()) {

                // If the player is already capturing, do not do anything.
                if(captureModel.isCapturing(teamPlayer)) return;

                FKTeam team = optionalTeamTo.get();

                // If the team is already eliminated, do no do anything.
                if(team.isEliminated()) return;

                onCaptureStart(optionalTeamTo.get(), teamPlayer);
            }
        }

        private Optional<? extends FKTeam> getEnemyVault(FKTeamPlayer player, Location location) {
            return model.getTeams().stream()
                    .filter(team -> !team.contains(player))
                    .filter(team -> team.getBase().getVault().isIn(location))
                    .findFirst();
        }
    }
}
