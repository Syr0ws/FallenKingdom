package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.model.placholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.v2.events.TeamPlayerAddEvent;
import com.github.syr0ws.fallenkingdom.game.model.v2.events.TeamPlayerRemoveEvent;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.displays.impl.Message;
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
    public void onPlayerTeamAdd(TeamPlayerAddEvent event) {

        FKTeam team = event.getTeam();
        FKTeamPlayer teamPlayer = event.getPlayer();

        Player player = teamPlayer.getPlayer();

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("team-messages");

        Message message = new Message(section.getString("join"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());
        message.displayTo(player);
    }

    @EventHandler
    public void onPlayerTeamRemove(TeamPlayerRemoveEvent event) {

        FKTeam team = event.getTeam();
        FKTeamPlayer teamPlayer = event.getPlayer();

        Player player = teamPlayer.getPlayer();

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("team-messages");

        Message message = new Message(section.getString("quit"));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());
        message.displayTo(player);
    }

}
