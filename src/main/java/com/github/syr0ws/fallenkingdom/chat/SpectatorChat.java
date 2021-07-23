package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.universe.commons.mode.DefaultModeType;
import com.github.syr0ws.universe.commons.modules.chat.Chat;
import com.github.syr0ws.universe.commons.modules.chat.ChatMessage;
import com.github.syr0ws.universe.commons.modules.chat.ChatPriority;
import com.github.syr0ws.universe.commons.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.mode.ModeType;
import com.github.syr0ws.universe.sdk.game.model.GamePlayer;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
import org.bukkit.entity.Player;

public class SpectatorChat implements Chat {

    private final FKModel model;
    private final FKSettings settings;

    public SpectatorChat(FKModel model) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        this.settings = model.getSettings();
    }

    @Override
    public void onChat(ChatMessage message) {

        String format = this.settings.getSpectatorChatFormatSetting().getValue();

        // Creating the message.
        Message msg = new Message(format);
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), message.getPlayer().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE.get(), message.getMessage());

        // Displaying the message to all the spectators.
        this.model.getOnlinePlayers().stream()
                .filter(gamePlayer -> gamePlayer.getModeType().equals(DefaultModeType.SPECTATOR))
                .forEach(gamePlayer -> msg.displayTo(gamePlayer.getPlayer()));
    }

    @Override
    public boolean canSend(ChatMessage message) {

        Player player = message.getPlayer();
        GamePlayer gamePlayer = this.model.getPlayer(player.getUniqueId());

        ModeType modeType = gamePlayer.getModeType();

        return this.isChatAllowed() && modeType.equals(DefaultModeType.SPECTATOR);
    }

    @Override
    public ChatPriority getPriority() {
        return ChatPriority.NORMAL;
    }

    private boolean isChatAllowed() {
        MutableSetting<Boolean> setting = this.settings.getAllowSpectatorChatSetting();
        return setting.getValue();
    }
}
