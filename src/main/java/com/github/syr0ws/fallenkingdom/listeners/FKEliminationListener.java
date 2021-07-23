package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.events.PlayerEliminateEvent;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamState;
import com.github.syr0ws.universe.commons.mode.DefaultModeType;
import com.github.syr0ws.universe.commons.modules.combat.events.GamePlayerDeathEvent;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.game.model.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class FKEliminationListener implements Listener {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public FKEliminationListener(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
        this.controller = game.getGameController();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        // Checking if the player is a FKTeamPlayer.
        if(!optional.isPresent()) return;

        FKTeamPlayer teamPlayer = optional.get();
        FKTeam fkTeam = teamPlayer.getTeam();

        // Only alive players are interesting us.
        if(!teamPlayer.isAlive()) return;

        long onlinePlayers = fkTeam.getOnlineTeamPlayers().stream()
                // Current player is still considered as online in this event.
                // So, even if he's the last only remaining online player of the
                // team, the size of the list will be 1 without this filter.
                .filter(online -> !online.equals(teamPlayer))
                .count();

        // Checking if there is no online players in the team.
        if(onlinePlayers == 0) {

            try { this.controller.eliminate(fkTeam);
            } catch (GameException e) { e.printStackTrace(); }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEliminate(PlayerEliminateEvent event) {

        FKTeamPlayer teamPlayer = event.getTeamPlayer();
        GamePlayer gamePlayer = this.model.getPlayer(teamPlayer.getUUID());

        // If the player is online, killing him.
        if(gamePlayer.isOnline()) gamePlayer.getPlayer().setHealth(0);

        // Setting player in spectator mode.
        this.controller.setMode(gamePlayer, DefaultModeType.SPECTATOR);

        // Teleporting player.
        if(gamePlayer.isOnline()) gamePlayer.getPlayer().teleport(this.model.getSpawn());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(GamePlayerDeathEvent event) {

        // Handling elimination case.
        this.handleElimination(event.getPlayer());
    }

    private void handleElimination(Player player) {

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        // Only a FKTeamPlayer can be eliminated.
        if(!optional.isPresent()) return;

        FKTeamPlayer teamPlayer = optional.get();
        FKTeam fkTeam = teamPlayer.getTeam();

        // Only an alive player can be eliminated.
        if(!teamPlayer.isAlive()) return;

        // Players are eliminated only when their base is captured.
        if(fkTeam.getState() != TeamState.BASE_CAPTURED) return;

        try { this.controller.eliminate(teamPlayer);
        } catch (GameException e) { e.printStackTrace(); }
    }
}
