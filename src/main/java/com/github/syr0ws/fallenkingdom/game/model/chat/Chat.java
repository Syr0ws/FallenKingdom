package com.github.syr0ws.fallenkingdom.game.model.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface Chat {

    void onPlayerChat(AsyncPlayerChatEvent event);
}
