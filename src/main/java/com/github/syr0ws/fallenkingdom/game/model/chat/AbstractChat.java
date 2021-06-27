package com.github.syr0ws.fallenkingdom.game.model.chat;

import com.github.syr0ws.fallenkingdom.game.model.placholders.GlobalPlaceholder;
import com.github.syr0ws.universe.displays.impl.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class AbstractChat implements Chat {

    protected Message getFormat(Player player, String msg, String format) {

        Message message = new Message(format);

        message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, player.getName());
        message.addPlaceholder(GlobalPlaceholder.DISPLAY_NAME, player.getDisplayName());
        message.addPlaceholder(GlobalPlaceholder.MESSAGE, ChatColor.stripColor(msg));

        return message;
    }
}
