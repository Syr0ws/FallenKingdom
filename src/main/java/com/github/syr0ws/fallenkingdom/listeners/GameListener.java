package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.modules.combat.events.GamePlayerRespawnEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GameListener implements Listener {

    private final FKModel model;

    public GameListener(FKModel model) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(GamePlayerRespawnEvent event) {

        Location spawn = this.model.getSpawn();
        event.setRespawnLocation(spawn); // Using game spawn as default respawn location.
    }
}
