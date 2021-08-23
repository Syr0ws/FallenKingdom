package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.api.game.mode.ModeType;
import com.github.syr0ws.universe.api.game.model.GamePlayer;
import com.github.syr0ws.universe.api.settings.MutableSetting;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.mode.DefaultModeType;
import com.github.syr0ws.universe.sdk.modules.chat.Chat;
import com.github.syr0ws.universe.sdk.modules.chat.ChatException;
import com.github.syr0ws.universe.sdk.modules.chat.ChatMessage;
import com.github.syr0ws.universe.sdk.modules.chat.ChatPriority;
import com.github.syr0ws.universe.sdk.placeholders.PlaceholderEnum;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeamChat implements Chat {

    private final FKModel model;

    public TeamChat(FKModel model) {
        this.model = model;
    }

    @Override
    public void onChat(ChatMessage message) {

        Player player = message.getSender();

        FKSettings settings = this.model.getSettings();
        String format = settings.getTeamChatFormatSetting().getValue();

        Optional<? extends FKTeamPlayer> optional = this.model.getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new ChatException("FKTeamPlayer not found.");

        FKTeamPlayer teamPlayer = optional.get();
        FKTeam team = teamPlayer.getTeam();

        // Creating the message.
        Message msg = new Message(format);
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), message.getSender().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE.get(), message.getMessage());
        msg.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), team.getDisplayName());

        // Sending it.
        Collection<Player> receivers = team.getOnlineTeamPlayers().stream()
                .map(FKTeamPlayer::getPlayer)
                .collect(Collectors.toList());

        message.setFormat(msg.getText());
        message.setReceivers(receivers);
    }

    @Override
    public boolean canSend(ChatMessage message) {

        if(!this.isChatAllowed()) return false;

        if(!this.model.isRunning()) return false;

        Player player = message.getSender();
        GamePlayer gamePlayer = this.model.getPlayer(player.getUniqueId());

        ModeType modeType = gamePlayer.getModeType();

        if(!modeType.equals(DefaultModeType.PLAYING)) return false;

        MutableSetting<Character> setting = this.model.getSettings().getTeamChatPrefixSetting();
        String msg = message.getMessage();

        return msg.startsWith(setting.getValue().toString());
    }

    @Override
    public ChatPriority getPriority() {
        return ChatPriority.HIGH;
    }

    private boolean isChatAllowed() {
        FKSettings settings = this.model.getSettings();
        return settings.getAllowTeamChatSetting().getValue();
    }
}
