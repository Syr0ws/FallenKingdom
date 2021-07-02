package com.github.syr0ws.fallenkingdom.game.model.chat;

import com.github.syr0ws.fallenkingdom.game.model.v2.settings.SettingAccessor;
import com.github.syr0ws.universe.displays.impl.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class WaitingChat extends AbstractChat {

    private final GameModel game;

    public WaitingChat(GameModel game) {
        this.game = game;
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        // Checking if spectator chat is enabled.
        if(!this.isChatAllowed()) return;

        SettingAccessor accessor = this.game.getSettings();

        String format = accessor.getWaitingChatFormatSetting().getValue();

        Message message = this.getFormat(event.getPlayer(), event.getMessage(), format);

        // Displaying the message to all the non playing players.
        Bukkit.getOnlinePlayers().forEach(message::displayTo);
    }

    private boolean isChatAllowed() {
        return this.game.getSettings().getAllowWaitingChatSetting().getValue();
    }
}
