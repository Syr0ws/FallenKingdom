package com.github.syr0ws.fallenkingdom.plugin.chat;

import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.plugin.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.sdk.chat.DefaultPlayingChat;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.modules.chat.ChatException;
import com.github.syr0ws.universe.sdk.modules.chat.ChatMessage;
import com.github.syr0ws.universe.sdk.modules.chat.ChatPriority;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayingChat extends DefaultPlayingChat {

    public PlayingChat(FKModel model) {
        super(model);
    }

    @Override
    public void onChat(ChatMessage message) {
        super.onChat(message);

        Player player = message.getSender();
        FKModel model = this.getModel();

        Optional<? extends FKTeamPlayer> optional = model.getTeamPlayer(player.getUniqueId());

        if(!optional.isPresent())
            throw new ChatException("FKTeamPlayer not found.");

        FKTeamPlayer teamPlayer = optional.get();
        FKTeam team = teamPlayer.getTeam();

        // Creating the message.
        Message msg = new Message(message.getFormat());
        msg.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), team.getDisplayName());

        message.setFormat(msg.getText());
    }

    @Override
    public ChatPriority getPriority() {
        return ChatPriority.LOW;
    }

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }
}
