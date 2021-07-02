package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.v2.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.events.GamePlayerJoinEvent;
import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
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

public class GamePlayerListener implements Listener {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public GamePlayerListener(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
        this.controller = game.getGameController();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {

        GamePlayer player = event.getGamePlayer();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUUID());

        if(!optional.isPresent()) this.controller.setMode(player, DefaultModeType.SPECTATOR);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        if(!(damager instanceof Player && damaged instanceof Player)) return;

        // If pvp is not enabled, cancelling the damage.
        if(!this.model.isPvPEnabled()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFriendlyFire(EntityDamageByEntityEvent event) {

        if(event.isCancelled()) return;

        SettingAccessor accessor = this.model.getSettings();
        Setting<Boolean> setting = accessor.getFriendlyFireSetting();

        // Friendly fire activated. Do not do anything.
        if(setting.getValue()) return;

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        Optional<? extends FKTeamPlayer> optional1 = this.model.getTeamPlayer(damaged.getUniqueId());
        Optional<? extends FKTeamPlayer> optional2 = this.model.getTeamPlayer(damager.getUniqueId());

        if(!optional1.isPresent() || !optional2.isPresent()) return;

        FKTeamPlayer player1 = optional1.get();
        FKTeamPlayer player2 = optional2.get();

        if(player1.getTeam().equals(player2.getTeam())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        // If the player isn't playing, using the game spawn as respawn location.
        if(!optional.isPresent()) {
            event.setRespawnLocation(this.model.getSpawn());
            return;
        }

        FKTeamPlayer teamPlayer = optional.get();
        SettingAccessor settings = this.model.getSettings();

        Location respawn;

        if(!teamPlayer.isAlive()) {

            // Using game spawn as respawn point.
            respawn = this.model.getSpawn();

        } else if(player.getBedSpawnLocation() != null && settings.getAllowRespawnBedSetting().getValue()) {

            // Using bed location as respawn point.
            respawn = player.getBedSpawnLocation();

        } else {

            // Using base spawn as respawn point.
            respawn = teamPlayer.getTeam().getBase().getSpawn();
        }

        event.setRespawnLocation(respawn);
    }
}
