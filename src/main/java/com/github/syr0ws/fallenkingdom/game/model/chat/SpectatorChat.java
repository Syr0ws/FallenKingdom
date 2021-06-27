package com.github.syr0ws.fallenkingdom.game.model.chat;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.universe.displays.impl.Message;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpectatorChat extends AbstractChat {

    private final GameModel game;

    public SpectatorChat(GameModel game) {

        if(game == null)
            throw new IllegalArgumentException("GameModel cannot be null.");

        this.game = game;
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        // Checking if spectator chat is enabled.
        if(!this.isChatAllowed()) return;

        SettingAccessor accessor = this.game.getSettings();

        String format = accessor.getSpectatorChatFormatSetting().getValue();

        Message message = this.getFormat(event.getPlayer(), event.getMessage(), format);

        // Displaying the message to all the non playing players.
        this.game.getOnlinePlayers().stream()
                .filter(gamePlayer -> !gamePlayer.isPlaying())
                .forEach(gamePlayer -> message.displayTo(gamePlayer.getPlayer()));
    }

    private boolean isChatAllowed() {
        return this.game.getSettings().getAllowSpectatorChatSetting().getValue();
    }
}
