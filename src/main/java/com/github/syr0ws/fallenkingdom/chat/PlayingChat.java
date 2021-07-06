package com.github.syr0ws.fallenkingdom.chat;

import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.displays.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.modules.chat.Chat;
import com.github.syr0ws.universe.modules.chat.ChatException;
import com.github.syr0ws.universe.modules.chat.ChatMessage;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayingChat implements Chat {

    private final FKModel model;
    private final SettingAccessor settings;

    public PlayingChat(FKModel model) {

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

        String format = this.settings.getGameChatFormatSetting().getValue();

        // Creating the message.
        Message msg = new Message(format);
        msg.addPlaceholder(PlaceholderEnum.PLAYER_NAME, message.getPlayer().getName());
        msg.addPlaceholder(PlaceholderEnum.MESSAGE, message.getMessage());
        msg.addPlaceholder(FKPlaceholder.TEAM_NAME, team.getDisplayName());

        // Displaying the message to all the online players.
        Bukkit.getOnlinePlayers().forEach(msg::displayTo);
    }

    @Override
    public boolean canSend(ChatMessage message) {

        Player player = message.getPlayer();
        FKPlayer fkPlayer = this.model.getPlayer(player.getUniqueId());

        return this.isChatAllowed() && this.model.isRunning() && fkPlayer.isPlaying();
    }

    private boolean isChatAllowed() {
        MutableSetting<Boolean> setting = this.settings.getAllowGameChatSetting();
        return setting.getValue();
    }
}
