package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.commons.mode.DefaultModeType;
import com.github.syr0ws.universe.commons.modules.chat.Chat;
import com.github.syr0ws.universe.commons.modules.chat.ChatException;
import com.github.syr0ws.universe.commons.modules.chat.ChatMessage;
import com.github.syr0ws.universe.commons.modules.chat.ChatPriority;
import com.github.syr0ws.universe.commons.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.mode.ModeType;
import com.github.syr0ws.universe.sdk.game.model.GamePlayer;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeamChat implements Chat {

    private final FKModel model;
    private final FKSettings settings;

    public TeamChat(FKModel model) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.model = model;
        this.settings = model.getSettings();
    }

    @Override
    public void onChat(ChatMessage message) {

        Player player = message.getPlayer();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new ChatException("FKTeamPlayer not found.");

        FKTeamPlayer teamPlayer = optional.get();
        FKTeam team = teamPlayer.getTeam();

        String format = this.settings.getTeamChatFormatSetting().getValue();

        // Creating the message.
        Message msg = new Message(format);
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), message.getPlayer().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE.get(), message.getMessage());
        msg.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), team.getDisplayName());

        // Sending it.
        team.sendDisplay(msg);
    }

    @Override
    public boolean canSend(ChatMessage message) {

        if(!this.isChatAllowed()) return false;

        if(!this.model.isRunning()) return false;

        Player player = message.getPlayer();
        GamePlayer gamePlayer = this.model.getPlayer(player.getUniqueId());

        ModeType modeType = gamePlayer.getModeType();

        if(!modeType.equals(DefaultModeType.PLAYING)) return false;

        MutableSetting<Character> setting = this.settings.getTeamChatPrefixSetting();
        String msg = message.getMessage();

        return msg.startsWith(setting.getValue().toString());
    }

    @Override
    public ChatPriority getPriority() {
        return ChatPriority.HIGH;
    }

    private boolean isChatAllowed() {
        MutableSetting<Boolean> setting = this.settings.getAllowWaitingChatSetting();
        return setting.getValue();
    }
}
