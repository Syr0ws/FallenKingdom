package com.github.syr0ws.fallenkingdom.game.cycle.impl;

import com.github.syr0ws.fallenkingdom.display.types.Message;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamBase;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.views.BoardManager;
import com.github.syr0ws.fallenkingdom.views.GameBoard;
import com.github.syr0ws.fallenkingdom.views.Scoreboard;
import com.github.syr0ws.fallenkingdom.views.ScoreboardManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class RunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel model;
    private final ScoreboardManager manager;

    public RunningCycle(GameModel model, Plugin plugin) {
        this.plugin = plugin;
        this.model = model;
        this.manager = new BoardManager();
    }

    @Override
    public void start() {

        super.addListener(new PlayerListener());
        super.addListener(new BlockListener());
        super.registerListeners(this.plugin);
        this.setupPlayers();
    }

    @Override
    public void stop() {

        super.unregisterListeners();
        super.clearListeners();
    }

    private void setupPlayers() {

        for(TeamPlayer teamPlayer : this.model.getPlayers()) {

            Player player = teamPlayer.getPlayer();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.setExp(0);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();

            Team team = teamPlayer.getTeam();
            TeamBase base = team.getBase();

            player.teleport(base.getSpawn().toBukkitLocation());
            player.setPlayerListName(team.getDisplayName() + " " + player.getName());

            GameBoard scoreboard = new GameBoard(player, this.plugin, this.model);
            scoreboard.update();

            this.model.addObserver(scoreboard);
            this.manager.addScoreboard(player, scoreboard);
        }
    }

    private void setViews(Player player) {

        Optional<Team> optional = this.model.getTeam(player);
        optional.ifPresent(team -> player.setPlayerListName(team.getDisplayName() + " " + player.getName()));

        this.setScoreboard(player);
    }

    private void setScoreboard(Player player) {

        GameBoard scoreboard = new GameBoard(player, this.plugin, this.model);
        scoreboard.update();

        this.model.addObserver(scoreboard);
        this.manager.addScoreboard(player, scoreboard);
    }

    private void removeScoreboard(Player player) {

        Optional<Scoreboard> optional = this.manager.getScoreboard(player);
        optional.map(scoreboard -> (GameBoard) scoreboard).ifPresent(scoreboard -> {
            this.model.removeObserver(scoreboard);
            manager.removeScoreboard(player);
        });
    }

    private class PlayerListener implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {

            Player player = event.getPlayer();

            setViews(player);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            removeScoreboard(player);
        }

        @EventHandler
        public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {

            Entity victim = event.getEntity();
            Entity damager = event.getDamager();

            if(!(victim instanceof Player && damager instanceof Player)) return;

            if(!model.isPvPEnabled()) {

                event.setCancelled(true);

                FileConfiguration config = plugin.getConfig();
                ConfigurationSection section = config.getConfigurationSection("game-messages.other");

                new Message(section.getString("pvp")).displayTo(damager);
            }
        }
    }

    private class BlockListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockPlace(BlockPlaceEvent event) {

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

                FileConfiguration config = plugin.getConfig();
                ConfigurationSection section = config.getConfigurationSection("game-messages.blocks");

                new Message(section.getString("place-not-allowed")).displayTo(player);

            } else {

                boolean allowed = this.canBePlaced(block);

                // If the block isn't allowed, cancelling the event.
                if(!allowed) {

                    event.setCancelled(true);

                    FileConfiguration config = plugin.getConfig();
                    ConfigurationSection section = config.getConfigurationSection("game-messages.blocks");

                    new Message(section.getString("place-not-allowed")).displayTo(player);
                }
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockBreak(BlockBreakEvent event) {

            Player player = event.getPlayer();

            // Cancelling the event if the player is trying to break a block inside an enemy base.
            if(model.isInsideEnemyBase(player)) {

                event.setCancelled(true);

                FileConfiguration config = plugin.getConfig();
                ConfigurationSection section = config.getConfigurationSection("game-messages.blocks");

                new Message(section.getString("break-in-enemy-base")).displayTo(player);
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBlockExplode(BlockExplodeEvent event) {

            // TODO To fix.

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
