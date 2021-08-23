package com.github.syr0ws.fallenkingdom.plugin.listeners;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class FKGameListener implements Listener {

    private final FKModel model;

    public FKGameListener(FKModel model) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortalUse(PlayerPortalEvent event) {

        World to = event.getTo().getWorld();
        World.Environment environment = to.getEnvironment();

        if(environment == World.Environment.NETHER && !this.model.isNetherEnabled()) {

            // If the player is going to the nether but nether is not enabled,
            // cancelling the event.
            event.setCancelled(true);

        } else if(environment == World.Environment.THE_END && !this.model.isEndEnabled()) {

            // If the player is going to the end but end is not enabled,
            // cancelling the event.
            event.setCancelled(true);
        }
    }
}
