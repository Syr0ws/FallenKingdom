package com.github.syr0ws.fallenkingdom.plugin.game.view.states.running;

import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.universe.api.game.view.GameView;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.Text;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.PluginManager;

public class FKGameView implements GameView, Listener {

    private final FKGame game;
    private final LangService service;

    public FKGameView(FKGame game, LangService service) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.game = game;
        this.service = service;
    }

    @Override
    public void enable() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this, this.game);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.KICK_FULL) return;

        Text text = this.service.getMessage("kick-full", Text.class);
        event.setKickMessage(text.getText());
    }
}
