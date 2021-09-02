package com.github.syr0ws.fallenkingdom.plugin.game.view.types;

import com.github.syr0ws.fallenkingdom.api.events.TeamPlayerAddEvent;
import com.github.syr0ws.fallenkingdom.api.events.TeamPlayerRemoveEvent;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.plugin.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.plugin.game.view.displays.GameDisplayEnum;
import com.github.syr0ws.universe.api.GamePlugin;
import com.github.syr0ws.universe.api.displays.Display;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.api.game.view.GameView;
import com.github.syr0ws.universe.sdk.displays.DisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FKTeamView implements GameView, Listener {

    private final GamePlugin plugin;
    private final DisplayManager manager;

    public FKTeamView(GamePlugin plugin, DisplayManager manager) {

        if(plugin == null)
            throw new IllegalArgumentException("GamePlugin cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void enable() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this, this.plugin);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeamAdd(TeamPlayerAddEvent event) {

        FKTeamPlayer teamPlayer = event.getPlayer();

        // Checking that the player is online.
        if(!teamPlayer.isOnline()) return;

        Player player = event.getPlayer().getPlayer();

        this.sendDisplays(player, event.getTeam(), GameDisplayEnum.TEAM_ADD);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeamRemove(TeamPlayerRemoveEvent event) {

        FKTeamPlayer teamPlayer = event.getPlayer();

        // Checking that the player is online.
        if(!teamPlayer.isOnline()) return;

        Player player = event.getPlayer().getPlayer();

        this.sendDisplays(player, event.getTeam(), GameDisplayEnum.TEAM_REMOVE);
    }

    private void sendDisplays(Player player, FKTeam team, GameDisplayEnum displayEnum) {

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.TEAM_NAME.get(), team.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        DisplayUtils.sendDisplays(displays, player);
    }
}
