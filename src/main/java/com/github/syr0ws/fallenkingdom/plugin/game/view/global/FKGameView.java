package com.github.syr0ws.fallenkingdom.plugin.game.view.global;

import com.github.syr0ws.universe.api.GamePlugin;
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

    private final GamePlugin plugin;
    private final LangService service;

    public FKGameView(GamePlugin plugin, LangService service) {

        if(plugin == null)
            throw new IllegalArgumentException("GamePlugin cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.plugin = plugin;
        this.service = service;
    }

    @Override
    public void enable() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this, this.plugin);
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
