package com.github.syr0ws.fallenkingdom.game.model.chat;

import com.github.syr0ws.fallenkingdom.game.model.placeholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.game.model.v2.settings.SettingAccessor;
import com.github.syr0ws.universe.displays.impl.Message;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GameChat extends AbstractChat {

    private final GameModel game;
    private final TeamPlayer teamPlayer;

    public GameChat(GameModel game, TeamPlayer teamPlayer) {

        if(game == null)
            throw new IllegalArgumentException("Game cannot be null.");

        if(teamPlayer == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        this.game = game;
        this.teamPlayer = teamPlayer;
    }

    @Override
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if(this.isTeamMessage(event.getMessage())) this.onTeamChat(event);
        else this.onGameChat(event);
    }

    @Override
    protected Message getFormat(Player player, String msg, String format) {

        Team team = this.teamPlayer.getTeam();

        Message message = super.getFormat(player, msg, format);
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());

        return message;
    }

    private void onGameChat(AsyncPlayerChatEvent event) {

        SettingAccessor accessor = this.game.getSettings();

        // Checking if game chat is enabled.
        if(!accessor.getAllowGameChatSetting().getValue()) return;

        MutableSetting<String> setting = accessor.getGameChatFormatSetting();

        Message message = this.getFormat(event.getPlayer(), event.getMessage(), setting.getValue());

        // Displaying the message to all the online players.
        Bukkit.getOnlinePlayers().forEach(message::displayTo);
    }

    private void onTeamChat(AsyncPlayerChatEvent event) {

        SettingAccessor accessor = this.game.getSettings();

        // Checking if team chat is enabled.
        if(!accessor.getAllowTeamChatSetting().getValue()) return;

        MutableSetting<String> setting = accessor.getGameChatFormatSetting();

        Message message = this.getFormat(event.getPlayer(), event.getMessage(), setting.getValue());

        Team team = this.teamPlayer.getTeam();

        // Displaying the message to all the online players of the sender team.
        team.getOnlineTeamPlayers().forEach(player -> message.displayTo(player.getPlayer()));
    }

    private boolean isTeamMessage(String message) {
        MutableSetting<Character> setting = this.game.getSettings().getTeamChatPrefixSetting();
        return message.startsWith(setting.getValue().toString());
    }
}
