package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.universe.sdk.events.GamePlayerQuitEvent;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.game.model.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FKWaitingListener implements Listener {

    private final FKModel model;
    private final FKController controller;

    public FKWaitingListener(FKModel model, FKController controller) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        this.model = model;
        this.controller = controller;
    }

    @EventHandler
    public void onGamePlayerQuit(GamePlayerQuitEvent event) {

        GamePlayer player = event.getGamePlayer();

        if(!this.model.hasTeam(player.getUUID())) return;

        try { this.controller.removeTeam((FKPlayer) player);
        } catch (GameException e) { e.printStackTrace(); }
    }
}
