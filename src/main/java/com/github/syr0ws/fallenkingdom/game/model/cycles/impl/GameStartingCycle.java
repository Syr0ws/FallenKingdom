package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayFactory;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.impl.listeners.GamePreRunningListener;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;
import com.github.syr0ws.fallenkingdom.settings.dao.FKSettingDAO;
import com.github.syr0ws.fallenkingdom.settings.dao.SettingDAO;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;
import com.github.syr0ws.fallenkingdom.settings.manager.ConfigSettingManager;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.fallenkingdom.tools.Task;
import com.github.syr0ws.fallenkingdom.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class GameStartingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager listenerManager;
    private final SettingManager settingManager;

    private CycleTask task;

    public GameStartingCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.listenerManager = new ListenerManager(plugin);
        this.settingManager = this.loadSettings();
    }

    @Override
    public void start() {
        this.listenerManager.addListener(new GamePreRunningListener());
        this.startTask();
    }

    @Override
    public void stop() {
        this.listenerManager.removeListeners();
        this.stopTask();
    }

    @Override
    public GameState getState() {
        return GameState.STARTING;
    }

    private void startTask() {

        TimerActionManager manager = new TimerActionManager();
        this.loadDisplays(manager);

        Setting<Integer> durationSetting = this.settingManager.getGenericSetting(CycleSetting.TIMER_DURATION, Integer.class);

        this.task = new CycleTask(manager, durationSetting.getValue());
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private void loadDisplays(TimerActionManager manager) {

        ConfigurationSection section = this.getCycleSection();
        ConfigurationSection displaySection = section.getConfigurationSection("displays");

        for(String key : displaySection.getKeys(false)) {

            if(!Validate.isInt(key)) continue;

            if(!displaySection.isConfigurationSection(key)) continue;

            int time = Integer.parseInt(key);

            ConfigurationSection keySection = displaySection.getConfigurationSection(key);

            for(String displayKey : keySection.getKeys(false)) {

                Display display = DisplayFactory.getDisplay(keySection.getConfigurationSection(displayKey));
                manager.addAction(time, new DisplayAction(display));
            }
        }
    }

    private SettingManager loadSettings() {

        SettingDAO dao = new FKSettingDAO(this.plugin);
        SettingManager manager = new ConfigSettingManager();

        List<CycleSetting> settings = Arrays.asList(CycleSetting.values());
        settings.forEach(setting -> manager.addSetting(setting, setting.getSetting()));

        dao.readSettings("starting-cycle.settings", manager.getSettings());

        return manager;
    }

    private ConfigurationSection getCycleSection() {
        FileConfiguration config = this.plugin.getConfig();
        return config.getConfigurationSection("starting-cycle");
    }

    private class CycleTask extends Task {

        private final TimerActionManager manager;
        private int duration;

        public CycleTask(TimerActionManager manager, int duration) {
            this.manager = manager;
            this.duration = duration;
        }

        @Override
        public void run() {

            if(this.duration >= 0) {

                this.manager.executeActions(this.duration);
                this.duration--;

            } else GameStartingCycle.this.finish(); // Setting the cycle as finished.
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(GameStartingCycle.this.plugin, 0L, 20L);
        }
    }

    private enum CycleSetting implements SettingKey {

        TIMER_DURATION(new SimpleConfigSetting<>("timer-duration", 10, Integer.class));

        private final Setting<?> setting;

        CycleSetting(Setting<?> setting) {
            this.setting = setting;
        }

        public Setting<?> getSetting() {
            return this.setting;
        }

        @Override
        public String getKey() {
            return this.name();
        }
    }
}
