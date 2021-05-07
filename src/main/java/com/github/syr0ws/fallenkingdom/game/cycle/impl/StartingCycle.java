package com.github.syr0ws.fallenkingdom.game.cycle.impl;

import com.github.syr0ws.fallenkingdom.game.GameModel;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.tools.Task;
import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class StartingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel model;
    private final Listener listener;
    private CycleTask task;

    public StartingCycle(GameModel model, Plugin plugin) {
        this.plugin = plugin;
        this.model = model;
        this.listener = new CycleListener();
    }

    @Override
    public void start() {

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this.listener, this.plugin);

        this.startCycleTask();
    }

    @Override
    public void stop() {

        HandlerList.unregisterAll(this.listener);

        this.stopCycleTask();
    }

    private void startCycleTask() {

        ConfigurationSection section = this.getCycleSection();
        int duration = section.getInt("timer-duration");

        this.task = new CycleTask(duration);
        this.task.start();
    }

    private void stopCycleTask() {
        this.task.stop();
        this.task = null; // Avoid reuse.
    }

    private ConfigurationSection getCycleSection() {
        FileConfiguration config = this.plugin.getConfig();
        return config.getConfigurationSection("game-starting");
    }

    private class CycleListener implements Listener {

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

    private class CycleTask extends Task {

        private final int duration;
        private int time;

        public CycleTask(int duration) {
            this.duration = duration;
            this.time = duration;
        }

        @Override
        public void run() {

            if(this.time >= 0) {
                // Do stuff here.
                this.time--;

            } else {

                this.stop();
                setFinished(true);
            }
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(plugin, 0L, this.duration * 20L);
        }
    }
}
