package com.github.syr0ws.fallenkingdom.capture.area.listeners;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.game.model.placholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.v2.teams.FKTeamPlayer;
import com.github.syr0ws.universe.displays.impl.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaCaptureListener implements Listener {

    private final FKGame game;

    public AreaCaptureListener(FKGame game) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        this.game = game;
    }

    @EventHandler
    public void onPlayerCaptureBaseStart(PlayerBaseCaptureStartEvent event) {

        FKTeamPlayer player = event.getTeamPlayer();

        Message message = new Message(this.getCaptureSection().getString("player-start-capture", ""));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCapturedTeam().getDisplayName());

        message.displayTo(player.getPlayer());
    }

    @EventHandler
    public void onPlayerCaptureBaseStop(PlayerBaseCaptureStopEvent event) {

        FKTeamPlayer player = event.getTeamPlayer();

        Message message = new Message(this.getCaptureSection().getString("player-stop-capture", ""));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCapturedTeam().getDisplayName());

        message.displayTo(player.getPlayer());
    }

    @EventHandler
    public void onTeamBaseCaptureStart(TeamBaseCaptureStartEvent event) {

        Message message = new Message(this.getCaptureSection().getString("team-base-start-capture", ""));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCatcher().getDisplayName());

        event.getTeam().sendDisplay(message, FKTeamPlayer::isAlive);
    }

    @EventHandler
    public void onTeamBaseCaptureStop(TeamBaseCaptureStopEvent event) {

        Message message = new Message(this.getCaptureSection().getString("team-base-stop-capture", ""));
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, event.getCatcher().getDisplayName());

        event.getTeam().sendDisplay(message, FKTeamPlayer::isAlive);
    }

    private ConfigurationSection getCaptureSection() {
        return this.game.getConfig().getConfigurationSection("capture");
    }
}
