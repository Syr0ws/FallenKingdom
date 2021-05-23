package com.github.syr0ws.fallenkingdom.game.model.cycles.impl.listeners;

import com.github.syr0ws.fallenkingdom.events.GamePlayerJoinEvent;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.SpectatorMode;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Optional;

public class GameRunningPlayerListener implements Listener {

    private final GameModel game;

    public GameRunningPlayerListener(GameModel game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {

        GamePlayer gamePlayer = event.getPlayer();

        Optional<? extends TeamPlayer> optional = this.game.getTeamPlayer(event.getPlayer().getUUID());

        // Equivalent to GamePlayer#isPlaying().
        if(!optional.isPresent()) {
            event.setMode(new SpectatorMode(gamePlayer, this.game));
            return;
        }

        TeamPlayer teamPlayer = optional.get();

        Mode mode = teamPlayer.isAlive() ? gamePlayer.getMode() : new SpectatorMode(gamePlayer, this.game);

        event.setMode(mode);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        if(damager instanceof Player && damaged instanceof Player) {

            if(!this.game.isPvPEnabled()) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        Location location = player.getBedSpawnLocation();

        if(location != null) event.setRespawnLocation(location);
    }
}
