package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamBase;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
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

import java.util.List;
import java.util.Optional;

public class FKBlockListener implements Listener {

    private final FKGame game;
    private final FKModel model;

    public FKBlockListener(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
        this.model = game.getGameModel();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location location = block.getLocation();

        Optional<? extends FKTeam> optional = this.model.getTeam(player.getUniqueId());

        // If the player has no team, cancelling the event.
        if(!optional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        FKTeam team = optional.get();
        FKTeamBase base = team.getBase();

        // If the block is placed inside the player's base, allow him to place it.
        if(base.getBase().isIn(location)) return;

        // The placed block is outside the player base. If the block isn't allowed,
        // cancelling the event.
        if(!this.isBlockAllowed(block)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        // If the player has no team, cancelling the event.
        if(!this.model.hasTeam(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        // Players cannot break blocs manually in enemy bases.
        boolean inEnemyBase = this.model.getTeams().stream()
                .filter(team -> !team.contains(player.getUniqueId())) // Only checking the enemy bases.
                .map(team -> team.getBase().getBase()) // Mapping to Cuboid.
                .anyMatch(cuboid -> cuboid.isIn(block.getLocation())); // Checking if the broken block is in an enemy base.

        if(inEnemyBase) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {

        if(this.model.areAssaultsEnabled()) return;

        // Cancelling damages.
        event.setCancelled(true);

        // Cancelling block explosions.
        event.blockList().clear();
    }

    private boolean isBlockAllowed(Block block) {

        FKSettings accessor = this.model.getSettings();
        MutableSetting<List<Material>> setting = accessor.getAllowedBlocksSetting();

        return setting.getValue().contains(block.getType());
    }
}
