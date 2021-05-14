package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PreRunningListener implements Listener {

    private final Plugin plugin;
    private final GameModel game;

    public PreRunningListener(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(this.game.getSpawn());

        // TODO to fix.

        /*
        ConfigurationSection section = null;

        Message message = new Message(section.getString("messages.join"));
        message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, player.getName());

        event.setJoinMessage(message.getText());
        */
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        // TODO to fix.
        /*
        ConfigurationSection section = null;

        Message message = new Message(section.getString("messages.quit"));
        message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, player.getName());

        event.setQuitMessage(message.getText());
         */
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
