package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.displays.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.game.model.mode.ModeType;
import com.github.syr0ws.universe.modules.chat.Chat;
import com.github.syr0ws.universe.modules.chat.ChatMessage;
import com.github.syr0ws.universe.modules.chat.ChatPriority;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.entity.Player;

public class SpectatorChat implements Chat {

    private final FKModel model;
    private final SettingAccessor settings;

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
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME, message.getPlayer().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE, message.getMessage());

        // Displaying the message to all the spectators.
        this.model.getOnlinePlayers().stream()
                .filter(gamePlayer -> !gamePlayer.isPlaying())
                .forEach(gamePlayer -> msg.displayTo(gamePlayer.getPlayer()));
    }

    @Override
    public boolean canSend(ChatMessage message) {

        Player player = message.getPlayer();
        FKPlayer fkPlayer = this.model.getPlayer(player.getUniqueId());

        ModeType modeType = fkPlayer.getModeType();

        return this.isChatAllowed() && this.model.isRunning() && modeType.equals(DefaultModeType.SPECTATOR);
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
