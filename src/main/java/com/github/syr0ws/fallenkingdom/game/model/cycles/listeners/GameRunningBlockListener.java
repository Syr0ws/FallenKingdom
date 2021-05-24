package com.github.syr0ws.fallenkingdom.game.model.cycles.listeners;

import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamBase;
import com.github.syr0ws.fallenkingdom.settings.impl.MaterialSetting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Optional;

public class GameRunningBlockListener implements Listener {

    private final GameModel game;

    public GameRunningBlockListener(GameModel game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {

        if(!this.game.areAssaultsEnabled()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        Material material = block.getType();
        Location location = block.getLocation();

        Optional<Team> optional = this.game.getTeam(player.getUniqueId());

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
        if(!this.game.hasTeam(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        // Players cannot break blocs manually in enemy bases.
        boolean inEnemyBase = this.game.getTeams().stream()
                .filter(team -> !team.contains(player.getUniqueId())) // Only checking the enemy bases.
                .map(team -> team.getBase().getCuboid())
                .anyMatch(cuboid -> cuboid.isIn(block.getLocation()));

        if(inEnemyBase) event.setCancelled(true);
    }

    private boolean isAllowed(Material material) {

        SettingManager manager = this.game.getSettings();
        MaterialSetting setting = manager.getSetting(GameSettings.ALLOWED_BLOCKS, MaterialSetting.class);

        return setting.getValue().contains(material);
    }

    private boolean isChest(Material material) {

        SettingManager manager = this.game.getSettings();
        MaterialSetting setting = manager.getSetting(GameSettings.VAULT_BLOCKS, MaterialSetting.class);

        return setting.getValue().contains(material);
    }
}
