package com.github.syr0ws.fallenkingdom.plugin.listeners;

import com.github.syr0ws.fallenkingdom.api.events.TeamPlayerAddEvent;
import com.github.syr0ws.fallenkingdom.api.events.TeamPlayerRemoveEvent;
import com.github.syr0ws.fallenkingdom.api.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamListener implements Listener {

    private final LangService service;
    private final DisplayManager manager;

    public TeamListener(LangService service, DisplayManager manager) {

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.service = service;
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerTeamAdd(TeamPlayerAddEvent event) {

        FKTeamPlayer teamPlayer = event.getPlayer();

        // Checking that the player is online.
        if(!teamPlayer.isOnline()) return;

        Player player = event.getPlayer().getPlayer();

        // this.sendDisplays(player, event.getTeam(), GameDisplayEnum.TEAM_ADD);
    }

    @EventHandler
    public void onPlayerTeamRemove(TeamPlayerRemoveEvent event) {

        FKTeamPlayer teamPlayer = event.getPlayer();

        // Checking that the player is online.
        if(!teamPlayer.isOnline()) return;

        Player player = event.getPlayer().getPlayer();

        // this.sendDisplays(player, event.getTeam(), GameDisplayEnum.TEAM_REMOVE);
    }

    /*
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
     */
}
