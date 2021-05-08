package com.github.syr0ws.fallenkingdom.game.cycle.impl;

import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.messages.placeholders.GlobalPlaceholder;
import com.github.syr0ws.fallenkingdom.messages.types.SimpleMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
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
import org.bukkit.plugin.PluginManager;

public class WaitingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel model;
    private final Listener listener;

    public WaitingCycle(GameModel model, Plugin plugin) {
        this.plugin = plugin;
        this.model = model;
        this.listener = new CycleListener();
    }

    @Override
    public void start() {

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this.listener, this.plugin);
    }

    @Override
    public void stop() {

        HandlerList.unregisterAll(this.listener);
    }

    private ConfigurationSection getWaitingSection() {
        FileConfiguration config = this.plugin.getConfig();
        return config.getConfigurationSection("waiting-cycle");
    }

    private class CycleListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {

            Player player = event.getPlayer();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.teleport(model.getSpawn().toBukkitLocation());

            ConfigurationSection section = getWaitingSection();

            SimpleMessage message = new SimpleMessage(section.getString("messages.join"));
            message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, player.getName());

            event.setJoinMessage(message.getText());
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            ConfigurationSection section = getWaitingSection();

            SimpleMessage message = new SimpleMessage(section.getString("messages.quit"));
            message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, player.getName());

            event.setQuitMessage(message.getText());
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
}
