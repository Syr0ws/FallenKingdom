package com.github.syr0ws.fallenkingdom.capture.area.manager;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.capture.area.displays.AreaDisplayEnum;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.PlayerBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStartEvent;
import com.github.syr0ws.fallenkingdom.capture.area.events.TeamBaseCaptureStopEvent;
import com.github.syr0ws.fallenkingdom.capture.area.listeners.AreaCaptureListener;
import com.github.syr0ws.fallenkingdom.capture.area.model.AreaCapture;
import com.github.syr0ws.fallenkingdom.capture.area.model.AreaModel;
import com.github.syr0ws.fallenkingdom.capture.area.model.CraftAreaCaptureModel;
import com.github.syr0ws.fallenkingdom.capture.area.settings.CaptureSettingsAccessor;
import com.github.syr0ws.fallenkingdom.capture.area.settings.CraftCaptureSettingAccessor;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.utils.DisplayUtils;
import com.github.syr0ws.universe.displays.DisplayManager;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.listeners.ListenerManager;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import com.github.syr0ws.universe.tools.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CraftAreaCaptureManager implements CaptureManager {

    private final FKGame game;
    private final FKModel model;
    private final FKController controller;

    private final CraftAreaCaptureModel captureModel;

    private final ListenerManager listenerManager;
    private final DisplayManager displayManager;

    private CaptureTask task;

    public CraftAreaCaptureManager(FKGame game, FKModel model, FKController controller) {

        if(game == null)
            throw new IllegalArgumentException("FKGame cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        this.game = game;
        this.model = model;
        this.controller = controller;

        CaptureSettingsAccessor settings = new CraftCaptureSettingAccessor(game.getConfig());

        this.captureModel = new CraftAreaCaptureModel(settings);

        this.displayManager = this.loadDisplayManager();
        this.listenerManager = new ListenerManager(game);
    }

    @Override
    public void capture(FKTeam team, FKTeam catcher) throws GameException {

        if(team.isEliminated())
            throw new GameException("Team already eliminated.");

        if(catcher.isEliminated())
            throw new GameException("Catcher team eliminated.");

        this.captureModel.removeCapture(team);
        this.controller.setBaseCaptured(team, catcher);
    }

    @Override
    public void enable() {

        this.listenerManager.addListener(new CaptureListener());
        this.listenerManager.addListener(new AreaCaptureListener(this.displayManager));

        this.startTask();
    }

    @Override
    public void disable() {
        this.listenerManager.removeListeners();
        this.stopTask();
    }

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.AREA;
    }

    private void startTask() {

        CaptureSettingsAccessor accessor = this.captureModel.getSettings();
        MutableSetting<Integer> setting = accessor.getCaptureDurationSetting();

        this.task = new CaptureTask(setting.getValue());
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private void onCaptureStart(FKTeam captured, FKTeamPlayer player) throws GameException {

        if(this.captureModel.isCapturing(player))
            throw new GameException("Player already capturing.");

        if(!this.captureModel.canCapture(captured, player))
            throw new GameException("Player cannot capture this team.");

        PlayerBaseCaptureStartEvent playerBaseCaptureStartEvent = new PlayerBaseCaptureStartEvent(player, captured);
        Bukkit.getPluginManager().callEvent(playerBaseCaptureStartEvent);

        if(!this.captureModel.isCaptured(captured)) {

            TeamBaseCaptureStartEvent teamBaseCaptureStartEvent = new TeamBaseCaptureStartEvent(captured, player.getTeam());
            Bukkit.getPluginManager().callEvent(teamBaseCaptureStartEvent);
        }

        this.captureModel.capture(player, captured);
    }

    private void onCaptureStop(FKTeamPlayer player) throws GameException {

        if(!this.captureModel.isCapturing(player))
            throw new GameException("Player not capturing.");

        AreaCapture capture = this.captureModel.removeCapture(player);

        PlayerBaseCaptureStopEvent playerBaseCaptureStopEvent = new PlayerBaseCaptureStopEvent(player, capture.getCapturedTeam());
        Bukkit.getPluginManager().callEvent(playerBaseCaptureStopEvent);

        if(!this.captureModel.isCaptured(capture.getCapturedTeam())) {

            TeamBaseCaptureStopEvent teamBaseCaptureStopEvent = new TeamBaseCaptureStopEvent(capture.getCapturedTeam(), capture.getCatcherTeam());
            Bukkit.getPluginManager().callEvent(teamBaseCaptureStopEvent);
        }
    }

    private DisplayManager loadDisplayManager() {

        // Creating manager.
        DisplayManager manager = DisplayUtils.getDisplayManager(this.game);

        // Loading displays.
        for(AreaDisplayEnum value : AreaDisplayEnum.values())
            manager.loadDisplays(value.getPath());

        return manager;
    }

    private class CaptureListener implements Listener {

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {

            Player player = event.getPlayer();
            Location from = event.getFrom(), to = event.getTo();

            // This allows less calculations.
            if(from.getBlock().equals(to.getBlock())) return;

            this.handleMove(player, from, to);
        }

        @EventHandler
        public void onPlayerTeleport(PlayerTeleportEvent event) {

            Player player = event.getPlayer();
            Location from = event.getFrom(), to = event.getTo();

            this.handleMove(player, from, to);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            Optional<? extends FKTeamPlayer> optionalPlayer = model.getTeamPlayer(player.getUniqueId());

            // Player is not a FKTeamPlayer (is not playing).
            if(!optionalPlayer.isPresent()) return;

            FKTeamPlayer teamPlayer = optionalPlayer.get();

            // Checking that the player is capturing a base.
            if(!captureModel.isCapturing(teamPlayer)) return;

            // If the player is capturing a base, stopping the capture.
            try { onCaptureStop(teamPlayer);
            } catch (GameException e) { e.printStackTrace(); }
        }

        private void handleMove(Player player, Location from, Location to) {

            // A vault can be captured only when assaults are enabled.
            if(!model.areAssaultsEnabled()) return;

            Optional<? extends FKTeamPlayer> optional = model.getTeamPlayer(player.getUniqueId());

            // Checking if the player is playing (is a FKTeamPlayer).
            if(!optional.isPresent()) return;

            FKTeamPlayer teamPlayer = optional.get();

            // Checking if the player is alive.
            if(!teamPlayer.isAlive()) return;

            Optional<? extends FKTeam> optionalTeamFrom = this.getEnemyVault(teamPlayer, from);
            Optional<? extends FKTeam> optionalTeamTo = this.getEnemyVault(teamPlayer, to);

            // First, checking if the player is leaving a vault.
            // If he's not, checking if the player is entering in a vault.
            if(optionalTeamFrom.isPresent() && !optionalTeamTo.isPresent()) {

                if(!captureModel.isCapturing(teamPlayer)) return;

                try { onCaptureStop(teamPlayer);
                } catch (GameException e) { e.printStackTrace(); }

            } else if(optionalTeamTo.isPresent() && !optionalTeamFrom.isPresent()) {

                // If the player is already capturing, do not do anything.
                if(captureModel.isCapturing(teamPlayer)) return;

                FKTeam team = optionalTeamTo.get();

                // If the team is already eliminated, do no do anything.
                if(team.isEliminated()) return;

                try { onCaptureStart(optionalTeamTo.get(), teamPlayer);
                } catch (GameException e) { e.printStackTrace(); }
            }
        }

        private Optional<? extends FKTeam> getEnemyVault(FKTeamPlayer player, Location location) {
            return model.getTeams().stream().filter(team -> {

                        if(team.contains(player)) return false;

                        AreaModel area = (AreaModel) team.getBase().getCapurable();

                        return area.getArea().isIn(location);

                    }).findFirst();
        }
    }

    private class CaptureTask extends Task {

        private final int captureDuration;

        public CaptureTask(int captureDuration) {

            if(captureDuration <= 0)
                throw new IllegalArgumentException("Capture duration must be positive.");

            this.captureDuration = captureDuration;
        }

        @Override
        public void run() {

            Collection<AreaCapture> captures = new ArrayList<>(captureModel.getCaptures());

            for(AreaCapture capture : captures) {

                if(!this.isCaptureComplete(capture)) continue;

                try { capture(capture.getCapturedTeam(), capture.getCatcherTeam());
                } catch (GameException e) { e.printStackTrace(); }
            }
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(game, 0L, 20L);
        }

        private boolean isCaptureComplete(AreaCapture capture) {
            return (System.currentTimeMillis() - capture.getStartTime()) >= this.captureDuration * 1000L;
        }
    }
}
