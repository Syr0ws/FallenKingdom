package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayFactory;
import com.github.syr0ws.fallenkingdom.game.GameSettings;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameRunningBlockListener;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameRunningPlayerListener;
import com.github.syr0ws.fallenkingdom.game.model.modes.impl.PlayingMode;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamPlayer;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.fallenkingdom.tools.Task;
import com.github.syr0ws.fallenkingdom.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class GameRunningCycle extends GameCycle {

    private final GameModel game;
    private final ScoreboardManager sbManager;

    private CycleTask task;

    public GameRunningCycle(Plugin plugin, GameModel game) {
        super(plugin);
        this.game = game;
        this.sbManager = new ScoreboardManager();
    }

    @Override
    public void load() {

        this.loadActions();
        this.loadDisplays();

        // Loading listeners.
        ListenerManager listenerManager = super.getListenerManager();

        listenerManager.addListener(new GameRunningPlayerListener(this.game));
        listenerManager.addListener(new GameRunningBlockListener(this.game));
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
        SettingManager settingManager = this.game.getSettings();

        Setting<Integer> pvpSetting = settingManager.getGenericSetting(GameSettings.PVP_ACTIVATION_TIME, Integer.class);
        Setting<Integer> assaultsSetting = settingManager.getGenericSetting(GameSettings.ASSAULTS_ACTIVATION_TIME, Integer.class);
        Setting<Integer> maxDurationSetting = settingManager.getGenericSetting(GameSettings.MAX_GAME_DURATION, Integer.class);

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> this.game.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> this.game.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::finish);
    }

    private void loadDisplays() {

        ConfigurationSection section = this.getCycleSection();
        ConfigurationSection displaySection = section.getConfigurationSection("displays");

        // May happens when no display are needed.
        if(displaySection == null) return;

        for(String key : displaySection.getKeys(false)) {

            if(!Validate.isInt(key)) continue;

            if(!displaySection.isConfigurationSection(key)) continue;

            int time = Integer.parseInt(key);

            ConfigurationSection keySection = displaySection.getConfigurationSection(key);

            for(String displayKey : keySection.getKeys(false)) {

                Display display = DisplayFactory.getDisplay(keySection.getConfigurationSection(displayKey));
                super.getActionManager().addAction(time, new DisplayAction(display));
            }
        }
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
