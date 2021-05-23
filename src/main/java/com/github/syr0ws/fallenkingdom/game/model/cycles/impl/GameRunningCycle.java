package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.listeners.GameRunningBlockListener;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.listeners.GameRunningPlayerListener;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.tools.Task;
import org.bukkit.plugin.Plugin;

public class GameRunningCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;

    private CycleTask task;
    private TimerActionManager actionManager;

    public GameRunningCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
        this.actionManager = this.getActionManager();
    }

    @Override
    public void start() {

        this.manager.addListener(new GameRunningPlayerListener(this.game));
        this.manager.addListener(new GameRunningBlockListener(this.game));

        this.startTask();
    }

    @Override
    public void stop() {

        this.manager.removeListeners();
        this.stopTask();
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }

    private void startTask() {
        this.task = new CycleTask(this.actionManager);
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private TimerActionManager getActionManager() {

        TimerActionManager actionManager = new TimerActionManager();

        // Retrieving settings.
        SettingManager settingManager = this.game.getSettings();

        Setting<Integer> pvpSetting = settingManager.getGenericSetting(GameSettings.PVP_ACTIVATION_TIME, Integer.class);
        Setting<Integer> assaultsSetting = settingManager.getGenericSetting(GameSettings.ASSAULTS_ACTIVATION_TIME, Integer.class);
        Setting<Integer> maxDurationSetting = settingManager.getGenericSetting(GameSettings.MAX_GAME_DURATION, Integer.class);

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> this.game.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> this.game.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::finish);

        return actionManager;
    }

    private class CycleTask extends Task {

        private final TimerActionManager manager;

        public CycleTask(TimerActionManager manager) {
            this.manager = manager;
        }

        @Override
        public void run() {

            GameModel game = GameRunningCycle.this.game;

            int time = game.getTime();

            this.manager.executeActions(time);

            game.addTime();
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(GameRunningCycle.this.plugin, 0L, 20L);
        }
    }
}
