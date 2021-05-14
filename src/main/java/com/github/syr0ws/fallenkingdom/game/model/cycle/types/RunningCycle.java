package com.github.syr0ws.fallenkingdom.game.model.cycle.types;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamBase;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.tools.Task;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class RunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;
    private CycleTask task;

    public RunningCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
    }

    @Override
    public void start() {

        this.startTask();

        this.manager.addListener(new PlayerListener());
        this.manager.addListener(new BlockListener());
    }

    @Override
    public void stop() {

        this.stopTask();

        this.manager.removeListeners();
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }

    private void startTask() {
        this.task = new CycleTask();
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private class CycleTask extends Task {

        @Override
        public void run() {

        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(RunningCycle.this.plugin, 0L, 20L);
        }
    }

    private class PlayerListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {

            Entity damaged = event.getEntity();
            Entity damager = event.getDamager();

            if(damager instanceof Player && damaged instanceof Player) {

                if(!RunningCycle.this.game.isPvPEnabled()) event.setCancelled(true);
            }
        }
    }

    private class BlockListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onEntityExplode(EntityExplodeEvent event) {

            if(!RunningCycle.this.game.areAssaultsEnabled()) event.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockPlace(BlockPlaceEvent event) {

            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();

            Optional<Team> optional = RunningCycle.this.game.getTeam(player);

            // If the player has no team, cancelling the event.
            if(!optional.isPresent()) {
                event.setCancelled(true);
                return;
            }

            Team team = optional.get();
            TeamBase base = team.getBase();

            // If the block is placed inside the player's base, allow him to place it.
            if(base.getCuboid().isIn(block.getLocation()))
                return;

            // The block is now placed outside the player base.
            // If the block isn't allowed, cancelling the event.
            if(!this.isAllowed(block)) event.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockBreak(BlockBreakEvent event) {

            Player player = event.getPlayer();
            Block block = event.getBlock();

            // If the player has no team, cancelling the event.
            if(!RunningCycle.this.game.hasTeam(player)) {
                event.setCancelled(true);
                return;
            }

            // Players cannot break blocs manually in enemy bases.
            boolean inEnemyBase = RunningCycle.this.game.getTeams().stream()
                    .filter(team -> !team.contains(player)) // Only checking the enemy bases.
                    .map(team -> team.getBase().getCuboid())
                    .anyMatch(cuboid -> cuboid.isIn(block.getLocation()));

            if(inEnemyBase) event.setCancelled(true);
        }

        private boolean isAllowed(Block block) {

            FileConfiguration config = RunningCycle.this.plugin.getConfig();
            List<String> blocks = config.getStringList("placeable-blocks");

            return blocks.contains(block.getType().name());
        }
    }
}
