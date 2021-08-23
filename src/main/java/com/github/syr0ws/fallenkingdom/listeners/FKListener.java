package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.modules.lang.messages.impl.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class FKListener implements Listener {

    private final LangService service;

    public FKListener(LangService service) {

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.service = service;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.KICK_FULL) return;

        Text text = this.service.getMessage("kick-full", Text.class);
        event.setKickMessage(text.getText());
    }
}
