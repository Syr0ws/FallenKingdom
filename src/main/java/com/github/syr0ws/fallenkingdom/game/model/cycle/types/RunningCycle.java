package com.github.syr0ws.fallenkingdom.game.model.cycle.types;

import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamBase;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.impl.MaterialSetting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.tools.Task;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

import java.util.Optional;

public class RunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;

    private CycleTask task;
    private TimerActionManager actionManager;

    public RunningCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
        this.actionManager = this.getActionManager();
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
        this.task = new CycleTask(this.actionManager);
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private TimerActionManager getActionManager() {

        TimerActionManager actionManager = new TimerActionManager();

        // Retrieving settings.
        SettingManager settingManager = this.game.getSettingManager();

        Setting<Integer> pvpSetting = settingManager.getGenericSetting(GameSettings.PVP_ACTIVATION_TIME, Integer.class);
        Setting<Integer> assaultsSetting = settingManager.getGenericSetting(GameSettings.ASSAULTS_ACTIVATION_TIME, Integer.class);
        Setting<Integer> maxDurationSetting = settingManager.getGenericSetting(GameSettings.MAX_GAME_DURATION, Integer.class);

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> this.game.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> this.game.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::finish);

        return actionManager;
    }

    private class CycleTask extends Task {

        private final TimerActionManager manager;

        public CycleTask(TimerActionManager manager) {
            this.manager = manager;
        }

        @Override
        public void run() {

            GameModel game = RunningCycle.this.game;

            int time = game.getTime();

            this.manager.executeActions(time);

            game.addTime();
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

            Material material = block.getType();
            Location location = block.getLocation();

            Optional<Team> optional = RunningCycle.this.game.getTeam(player);

            // If the player has no team, cancelling the event.
            if(!optional.isPresent()) {
                event.setCancelled(true);
                return;
            }

            Team team = optional.get();
            TeamBase base = team.getBase();

            // If the block is placed inside the player's base, allow him to place it
            // only if it is not a vault block.
            if(base.getCuboid().isIn(location)) {

                // If the block can only be placed in the vault and it isn't
                // cancelling the event.
                if(this.isChest(material) && !base.getVault().isIn(location)) event.setCancelled(true);

            // The block is now placed outside the player base.
            // If the block isn't allowed, cancelling the event.
            } else if(!this.isAllowed(material)) event.setCancelled(true);
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

        private boolean isAllowed(Material material) {

            SettingManager manager = RunningCycle.this.game.getSettingManager();
            MaterialSetting setting = manager.getSetting(GameSettings.ALLOWED_BLOCKS, MaterialSetting.class);

            return setting.getValue().contains(material);
        }

        private boolean isChest(Material material) {

            SettingManager manager = RunningCycle.this.game.getSettingManager();
            MaterialSetting setting = manager.getSetting(GameSettings.VAULT_BLOCKS, MaterialSetting.class);

            return setting.getValue().contains(material);
        }
    }
}
