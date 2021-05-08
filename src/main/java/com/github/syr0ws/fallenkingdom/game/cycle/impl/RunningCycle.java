package com.github.syr0ws.fallenkingdom.game.cycle.impl;

import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.teams.TeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Optional;

public class RunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel model;
    private final Listener listener;

    public RunningCycle(GameModel model, Plugin plugin) {
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

    private class CycleListener implements Listener {

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent event) {

            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();
            Location location = block.getLocation();

            Optional<Team> optional = model.getTeam(player);

            // Should not happen because all the players which can place blocks are in a team.
            if(!optional.isPresent()) {
                event.setCancelled(true);
                return;
            }

            Team team = optional.get();
            TeamBase base = team.getBase();

            // Checking if the placed block is in the base of the player.
            // If true, allow placing.
            if(base.getCuboid().isIn(location)) return;

            boolean allowed = this.canBePlaced(block);

            // If the block isn't allowed, cancelling the event.
            if(!allowed) event.setCancelled(true);
        }

        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {

            Player player = event.getPlayer();

            // Cancelling the event if the player is inside an enemy base.
            if(model.isInsideEnemyBase(player)) event.setCancelled(true);
        }

        @EventHandler
        public void onBlockExplode(BlockExplodeEvent event) {

            // Cancelling block explosions if assaults are not enabled.
            if(!model.areAssaultsEnabled()) event.setCancelled(true);
        }

        private boolean canBePlaced(Block block) {

            FileConfiguration config = plugin.getConfig();
            List<String> blocks = config.getStringList("placeable-blocks");

            return blocks.contains(block.getType().name());
        }
    }
}
