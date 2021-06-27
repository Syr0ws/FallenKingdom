package com.github.syr0ws.fallenkingdom.game.model.cycles.listeners;

import com.github.syr0ws.fallenkingdom.events.GamePlayerJoinEvent;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.SpectatorMode;
import com.github.syr0ws.fallenkingdom.game.model.players.GamePlayer;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.universe.settings.Setting;
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
    private final GameController controller;

    public GameRunningPlayerListener(GameModel game, GameController controller) {
        this.game = game;
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {

        GamePlayer gamePlayer = event.getPlayer();

        Optional<? extends TeamPlayer> optional = this.game.getTeamPlayer(gamePlayer);

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

        if(!(damager instanceof Player && damaged instanceof Player)) return;

        // If pvp is not enabled, cancelling the event.
        if(!this.game.isPvPEnabled()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFriendlyFire(EntityDamageByEntityEvent event) {

        if(event.isCancelled()) return;

        SettingAccessor accessor = this.game.getSettings();
        Setting<Boolean> setting = accessor.getFriendlyFireSetting();

        // Friendly fire activated. Do not do anything.
        if(setting.getValue()) return;

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        Optional<? extends TeamPlayer> optional1 = this.game.getTeamPlayer(damaged.getUniqueId());
        Optional<? extends TeamPlayer> optional2 = this.game.getTeamPlayer(damager.getUniqueId());

        if(!optional1.isPresent() || !optional2.isPresent()) return;

        TeamPlayer player1 = optional1.get();
        TeamPlayer player2 = optional2.get();

        if(player1.getTeam().equals(player2.getTeam())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        Optional<? extends TeamPlayer> optional = this.game.getTeamPlayer(player.getUniqueId());

        // Not a team player.
        if(!optional.isPresent()) {
            event.setRespawnLocation(this.game.getSpawn());
            return;
        }

        TeamPlayer teamPlayer = optional.get();

        Location respawn;

        if(!teamPlayer.isAlive()) respawn = this.game.getSpawn();
        else if(player.getBedSpawnLocation() != null) respawn = player.getBedSpawnLocation();
        else respawn = teamPlayer.getTeam().getBase().getSpawn();

        event.setRespawnLocation(respawn);
    }
}
