package com.github.syr0ws.fallenkingdom.game.cycle.impl;

import com.github.syr0ws.fallenkingdom.display.types.Message;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.teams.TeamBase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class RunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel model;

    public RunningCycle(GameModel model, Plugin plugin) {
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public void start() {

        super.addListener(new PlayerListener());
        super.addListener(new BlockListener());
        super.registerListeners(this.plugin);
    }

    @Override
    public void stop() {

        super.unregisterListeners();
        super.clearListeners();
    }

    private class PlayerListener implements Listener {

        @EventHandler
        public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {

            Entity victim = event.getEntity();
            Entity damager = event.getDamager();

            if(!(victim instanceof Player && damager instanceof Player)) return;

            if(!model.isPvPEnabled()) {

                event.setCancelled(true);

                new Message("").displayTo(damager); // TODO Send a message here.
            }
        }
    }

    private class BlockListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onNotAllowedBlockPlace(BlockPlaceEvent event) {

            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();

            boolean allowed = this.canBePlaced(block);

            // If the block isn't allowed, cancelling the event.
            if(!allowed) {

                event.setCancelled(true);

                new Message("").displayTo(player); // TODO Send a message here.
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockPlaceInsideBase(BlockPlaceEvent event) {

            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();
            Location location = block.getLocation();

            Optional<Team> optional = model.getTeam(player);

            if(!optional.isPresent()) {
                event.setCancelled(true);
                return;
            }

            Team team = optional.get();
            TeamBase base = team.getBase();

            // Checking if the placed block is in the base of the player. If true, allow placing.
            if(base.getCuboid().isIn(location)) return;

            // Players cannot place allowed blocs inside enemy bases while assaults are not enabled.
            if(!model.areAssaultsEnabled() && model.isInsideEnemyBase(player)) {

                event.setCancelled(true);

                new Message("").displayTo(player); // TODO Send a message here.
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockBreak(BlockBreakEvent event) {

            Player player = event.getPlayer();

            // Cancelling the event if the player is trying to break a block inside an enemy base.
            if(model.isInsideEnemyBase(player)) {

                event.setCancelled(true);

                new Message("").displayTo(player); // TODO Send a message here.
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockExplode(BlockExplodeEvent event) {

            event.blockList().clear();

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
