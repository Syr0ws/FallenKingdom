package com.github.syr0ws.fallenkingdom.messages;

import org.bukkit.entity.Player;

public interface Message {

    void send(Player player);

    void broadcast();
}
