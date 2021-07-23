package com.github.syr0ws.fallenkingdom.capture.area.listeners;

import com.github.syr0ws.fallenkingdom.capture.area.displays.AreaDisplayEnum;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.events.TeamBaseCapturedEvent;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.sdk.displays.Display;
import com.github.syr0ws.universe.sdk.displays.DisplayManager;
import com.github.syr0ws.universe.sdk.displays.DisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AreaCaptureListener implements Listener {

    private final DisplayManager manager;

    public AreaCaptureListener(DisplayManager manager) {

        if(manager == null)
            throw new IllegalArgumentException("DisplayManager cannot be null.");

        this.manager = manager;
    }

    @EventHandler
    public void onPlayerCaptureBaseStart(PlayerBaseCaptureStartEvent event) {

        FKTeamPlayer teamPlayer = event.getTeamPlayer();
        FKTeam captured = event.getCapturedTeam();

        Player player = teamPlayer.getPlayer();

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CAPTURED_TEAM_NAME.get(), captured.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.CAPTURE_START_SELF.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        DisplayUtils.sendDisplays(displays, player);
    }

    @EventHandler
    public void onPlayerCaptureBaseStop(PlayerBaseCaptureStopEvent event) {

        FKTeamPlayer teamPlayer = event.getTeamPlayer();
        FKTeam captured = event.getCapturedTeam();

        Player player = teamPlayer.getPlayer();

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CAPTURED_TEAM_NAME.get(), captured.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.CAPTURE_STOP_SELF.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        DisplayUtils.sendDisplays(displays, player);
    }

    @EventHandler
    public void onTeamBaseCaptureStart(TeamBaseCaptureStartEvent event) {

        FKTeam captured = event.getTeam();
        FKTeam catcher = event.getCatcher();

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CATCHER_TEAM_NAME.get(), catcher.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.CAPTURE_START_CAPTURED_TEAM.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        displays.forEach(display -> captured.sendDisplay(display, FKTeamPlayer::isAlive));
    }

    @EventHandler
    public void onTeamBaseCaptureStop(TeamBaseCaptureStopEvent event) {

        FKTeam captured = event.getTeam();
        FKTeam catcher = event.getCatcher();

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CATCHER_TEAM_NAME.get(), catcher.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.CAPTURE_STOP_CAPTURED_TEAM.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        displays.forEach(display -> captured.sendDisplay(display, FKTeamPlayer::isAlive));
    }

    @EventHandler
    public void onTeamBaseCapture(TeamBaseCapturedEvent event) {

        FKTeam captured = event.getTeam();
        FKTeam catcher = event.getCatcher();

        // Sending displays to the captured team.
        this.sendCapturedTeamDisplays(captured, catcher);

        // Sending displays to the catcher team.
        this.sendCatcherTeamDisplays(captured, catcher);

        // Sending displays to the other players.
        this.sendOtherDisplays(captured, catcher);
    }

    private void sendCatcherTeamDisplays(FKTeam captured, FKTeam catcher) {

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CAPTURED_TEAM_NAME.get(), captured.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.BASE_CAPTURED_SELF.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        displays.forEach(display -> catcher.sendDisplay(display, FKTeamPlayer::isAlive));
    }

    private void sendCapturedTeamDisplays(FKTeam captured, FKTeam catcher) {

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CATCHER_TEAM_NAME.get(), catcher.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.BASE_CAPTURED_CAPTURED_TEAM.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        displays.forEach(display -> captured.sendDisplay(display, FKTeamPlayer::isAlive));
    }

    private void sendOtherDisplays(FKTeam captured, FKTeam catcher) {

        // Handling placeholders.
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(FKPlaceholder.CAPTURED_TEAM_NAME.get(), captured.getDisplayName());
        placeholders.put(FKPlaceholder.CATCHER_TEAM_NAME.get(), catcher.getDisplayName());

        // Retrieving displays.
        Collection<Display> displays = this.manager.getDisplays(AreaDisplayEnum.BASE_CAPTURED_OTHER.getPath());

        // Adding placeholders.
        DisplayUtils.addPlaceholders(displays, placeholders);

        // Sending displays.
        List<Player> players = Bukkit.getOnlinePlayers().stream()
                .filter(player -> captured.contains(player.getUniqueId()) && catcher.contains(player.getUniqueId()))
                .collect(Collectors.toList());

        DisplayUtils.sendDisplays(displays, players);
    }
}
