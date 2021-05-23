package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import com.github.syr0ws.fallenkingdom.displays.placeholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.events.TeamPlayerAddEvent;
import com.github.syr0ws.fallenkingdom.events.TeamPlayerRemoveEvent;
import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class TeamListener implements Listener {

    private final Plugin plugin;

    public TeamListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeamPlayerAdd(TeamPlayerAddEvent event) {

        Team team = event.getTeam();
        TeamPlayer teamPlayer = event.getPlayer();

        Player player = event.getPlayer().getPlayer();

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("team-messages");

        Message message = new Message(section.getString("join"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());
        message.displayTo(player);
    }

    @EventHandler
    public void onTeamPlayerRemove(TeamPlayerRemoveEvent event) {

        Team team = event.getTeam();
        TeamPlayer teamPlayer = event.getPlayer();

        Player player = event.getPlayer().getPlayer();

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("team-messages");

        Message message = new Message(section.getString("quit"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());
        message.displayTo(player);
    }

}
