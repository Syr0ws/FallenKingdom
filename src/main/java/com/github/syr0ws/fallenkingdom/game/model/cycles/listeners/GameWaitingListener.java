package com.github.syr0ws.fallenkingdom.game.model.cycles.listeners;

import com.github.syr0ws.fallenkingdom.events.GamePlayerJoinEvent;
import com.github.syr0ws.fallenkingdom.events.GamePlayerLeaveEvent;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.GameWaitingCycle;
import com.github.syr0ws.fallenkingdom.game.model.modes.Mode;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.WaitingMode;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

public class GameWaitingListener implements Listener {

    private final Plugin plugin;
    private final ScoreboardManager sbManager;

    public GameWaitingListener(GameWaitingCycle cycle, ScoreboardManager sbManager) {
        this.plugin = cycle.getPlugin();
        this.sbManager = sbManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {

        Mode mode = new WaitingMode(event.getPlayer(), event.getGame(), this.plugin.getConfig(), sbManager);
        event.setMode(mode);

        this.sbManager.updateScoreboards();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerLeave(GamePlayerLeaveEvent event) {

        this.sbManager.updateScoreboards();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(event.getEntity() instanceof Player);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
