package com.github.syr0ws.fallenkingdom.plugin.listeners;

import com.github.syr0ws.fallenkingdom.api.events.TeamWinEvent;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.plugin.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.game.model.placeholders.FKPlaceholder;
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
import java.util.stream.Collectors;

public class FKTeamWinListener implements GameView, Listener {

    private final GamePlugin plugin;
    private final DisplayManager manager;

    public FKTeamWinListener(GamePlugin plugin, DisplayManager manager) {

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
    public void onTeamWin(TeamWinEvent event) {

        FKTeam team = event.getTeam();

        // Handling displays for all the online players.
        this.sendWinDisplaysAll(team);

        // Handling displays for the winning team.
        this.sendWinDisplaysTeam(team);

        // Handling displays for the other players.
        this.sendWinDisplaysOther(team);
    }

    private void sendWinDisplaysAll(FKTeam team) {

        Collection<Display> displays = this.getTeamWinDisplays(team, GameDisplayEnum.WIN_ALL);
        DisplayUtils.sendDisplays(displays, Bukkit.getOnlinePlayers());
    }

    private void sendWinDisplaysTeam(FKTeam team) {

        Collection<Display> displays = this.getTeamWinDisplays(team, GameDisplayEnum.WIN_SELF);
        displays.forEach(display -> team.sendDisplay(display, FKTeamPlayer::isAlive));
    }

    private void sendWinDisplaysOther(FKTeam team) {

        Collection<Display> displays = this.getTeamWinDisplays(team, GameDisplayEnum.WIN_OTHER);

        Collection<? extends Player> players = Bukkit.getOnlinePlayers().stream()
                .filter(player -> !team.contains(player.getUniqueId()))
                .collect(Collectors.toList());

        DisplayUtils.sendDisplays(displays, players);
    }

    private Collection<Display> getTeamWinDisplays(FKTeam team, GameDisplayEnum displayEnum) {

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.TEAM_NAME.get(), team.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(displayEnum.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        return displays;
    }
}
