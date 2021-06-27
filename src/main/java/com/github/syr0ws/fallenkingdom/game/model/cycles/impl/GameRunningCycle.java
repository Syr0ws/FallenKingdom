package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameCaptureListener;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameRunningBlockListener;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameRunningPlayerListener;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.PlayingMode;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.dao.TimerDisplayDAO;
import com.github.syr0ws.universe.settings.Setting;
import com.github.syr0ws.universe.tools.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;

public class GameRunningCycle extends GameCycle {

    private final GameModel game;
    private final GameController controller;
    private final ScoreboardManager sbManager;

    private CycleTask task;

    public GameRunningCycle(Plugin plugin, GameModel game, GameController controller) {
        super(plugin);
        this.game = game;
        this.controller = controller;
        this.sbManager = new ScoreboardManager();
    }

    @Override
    public void load() {

        this.loadActions();
        this.loadDisplays();

        // Loading listeners.
        ListenerManager listenerManager = super.getListenerManager();

        listenerManager.addListener(new GameRunningPlayerListener(this.game, this.controller));
        listenerManager.addListener(new GameRunningBlockListener(this.game));
        listenerManager.addListener(new GameCaptureListener(this.getPlugin(), this.controller));
    }

    @Override
    public void unload() {

        super.getListenerManager().removeListeners();
    }

    @Override
    public void start() {
        this.setPlayingMode();
        this.startTask();
    }

    @Override
    public void stop() {
        this.stopTask();
    }

    private void startTask() {
        this.task = new GameRunningCycle.CycleTask();
        this.task.start();
    }

    private void stopTask() {

        if(this.task == null || !this.task.isRunning()) return;

        this.task.stop();
        this.task = null;
    }

    private void loadActions() {

        TimerActionManager actionManager = super.getActionManager();

        // Retrieving settings.
        SettingAccessor accessor = this.game.getSettings();

        Setting<Integer> pvpSetting = accessor.getPvPActivationTimeSetting();
        Setting<Integer> assaultsSetting = accessor.getAssaultsActivationTimeSetting();
        Setting<Integer> maxDurationSetting = accessor.getMaxGameDurationSetting();

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> this.game.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> this.game.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::finish);
    }

    private void loadDisplays() {

        ConfigurationSection section = this.getCycleSection();

        TimerDisplayDAO dao = new TimerDisplayDAO(section);

        try {

            Map<Integer, Collection<Display>> displays = dao.getTimeDisplays("displays");

            displays.forEach((time, list) -> list.stream()
                    .map(DisplayAction::new)
                    .forEach(action -> super.getActionManager().addAction(time, action)));

        } catch (DisplayException e) { e.printStackTrace(); }
    }

    private void setPlayingMode() {

        for(TeamPlayer player : this.game.getTeamPlayers()) {

            PlayingMode mode = new PlayingMode(player, this.game, this.sbManager, super.getPlugin().getConfig());

            this.game.setGamePlayerMode(player.getUUID(), mode);
        }
    }

    private ConfigurationSection getCycleSection() {
        return super.getPlugin().getConfig().getConfigurationSection("running-cycle");
    }

    private class CycleTask extends Task {

        @Override
        public void run() {

            GameModel game = GameRunningCycle.this.game;

            int time = game.getTime();

            GameRunningCycle.this.getActionManager().executeActions(time);

            game.addTime();
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(GameRunningCycle.this.getPlugin(), 0L, 20L);
        }
    }
}
