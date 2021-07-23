package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamBase;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.commons.modules.combat.events.GamePlayerRespawnEvent;
import com.github.syr0ws.universe.sdk.settings.Setting;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.Optional;

public class FKPlayerListener implements Listener {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    public FKPlayerListener(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
        this.controller = game.getGameController();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {

        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        if(!(damager instanceof Player && damaged instanceof Player)) return;

        // If pvp is not enabled, cancelling the damage.
        if(!this.model.isPvPEnabled()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFriendlyFire(EntityDamageByEntityEvent event) {

        // If the damage is already cancelled, the event is handled.
        if(event.isCancelled()) return;

        // If pvp is not enabled, cancelling the damage.
        if(!this.model.isPvPEnabled()) event.setCancelled(true);

        FKSettings accessor = this.model.getSettings();
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(GamePlayerRespawnEvent event) {

        Player player = event.getPlayer();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        // If the player isn't playing, using the default spawn as respawn location.
        if(!optional.isPresent()) return;

        FKTeamPlayer teamPlayer = optional.get();
        FKSettings settings = this.model.getSettings();

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {

        Player player = event.getPlayer();
        Location location = event.getBed().getLocation();

        Optional<? extends FKTeam> optional = this.model.getTeam(player.getUniqueId());

        // If the player has no team, cancelling the event.
        if(!optional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        FKTeam team = optional.get();
        FKTeamBase base = team.getBase();

        // If the bed isn't into the player's base, cancelling the event.
        if(!base.getBase().isIn(location)) event.setCancelled(true);
    }
}
