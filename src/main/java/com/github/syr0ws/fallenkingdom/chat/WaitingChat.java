package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.universe.commons.modules.chat.Chat;
import com.github.syr0ws.universe.commons.modules.chat.ChatMessage;
import com.github.syr0ws.universe.commons.modules.chat.ChatPriority;
import com.github.syr0ws.universe.commons.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
import org.bukkit.Bukkit;

public class WaitingChat implements Chat {

    private final FKModel model;
    private final FKSettings settings;

    public WaitingChat(FKModel model) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        this.settings = model.getSettings();
    }

    @Override
    public void onChat(ChatMessage message) {

        // Retrieving format.
        String format = this.settings.getWaitingChatFormatSetting().getValue();

        // Creating the message.
        Message msg = new Message(format);
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), message.getPlayer().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE.get(), message.getMessage());

        // Sending it.
        Bukkit.getOnlinePlayers().forEach(msg::displayTo);
    }

    @Override
    public boolean canSend(ChatMessage chatMessage) {
        return this.isChatAllowed() && !this.model.isStarted();
    }

    @Override
    public ChatPriority getPriority() {
        return ChatPriority.LOW;
    }

    private boolean isChatAllowed() {
        MutableSetting<Boolean> setting = this.settings.getAllowWaitingChatSetting();
        return setting.getValue();
    }
}
