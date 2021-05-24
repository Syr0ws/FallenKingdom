package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayFactory;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameWaitingListener;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import com.github.syr0ws.fallenkingdom.settings.SettingKey;
import com.github.syr0ws.fallenkingdom.settings.dao.FKSettingDAO;
import com.github.syr0ws.fallenkingdom.settings.dao.SettingDAO;
import com.github.syr0ws.fallenkingdom.settings.impl.SimpleConfigSetting;
import com.github.syr0ws.fallenkingdom.settings.manager.ConfigSettingManager;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.fallenkingdom.tools.Task;
import com.github.syr0ws.fallenkingdom.tools.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameWaitingCycle extends GameCycle implements AttributeObserver {

    private final GameModel game;
    private final ScoreboardManager sbManager = new ScoreboardManager();
    private final SettingManager settings = new ConfigSettingManager();

    private CycleTask task;

    public GameWaitingCycle(Plugin plugin, GameModel game) {
        super(plugin);
        this.game = game;
    }

    @Override
    public void load() {

        this.loadSettings();
        this.loadDisplays();

        ListenerManager listenerManager = super.getListenerManager();
        listenerManager.addListener(new GameWaitingListener(this));

        this.game.addObserver(this);
    }

    @Override
    public void unload() {

        super.getListenerManager().removeListeners();

        this.game.removeObserver(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

        this.stopTask();
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameState state = this.game.getState();

        if(state == GameState.WAITING) this.stopTask();
        else if(state == GameState.STARTING) this.startTask();

        System.out.println("change !");
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.STATE_CHANGE);
    }

    public void startTask() {

        Setting<Integer> durationSetting = this.settings.getGenericSetting(CycleSetting.TIMER_DURATION, Integer.class);

        this.task = new CycleTask(durationSetting.getValue());
        this.task.start();
    }

    public void stopTask() {

        if(this.task == null || !this.task.isRunning()) return;

        this.task.stop();
        this.task = null;
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

    private void loadSettings() {

        SettingDAO dao = new FKSettingDAO(this.getPlugin());

        List<CycleSetting> settings = Arrays.asList(CycleSetting.values());
        settings.forEach(setting -> this.settings.addSetting(setting, setting.getSetting()));

        dao.readSettings("waiting-cycle.settings", this.settings.getSettings());
    }

    private ConfigurationSection getCycleSection() {
        return this.getPlugin().getConfig().getConfigurationSection("waiting-cycle");
    }

    private class CycleTask extends Task {

        private int duration;

        public CycleTask(int duration) {
            this.duration = duration;
        }

        @Override
        public void run() {

            if(this.duration >= 0) {

                GameWaitingCycle.this.getActionManager().executeActions(this.duration);
                this.duration--;

            } else GameWaitingCycle.this.finish(); // Setting the cycle as finished.
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(GameWaitingCycle.this.getPlugin(), 0L, 20L);
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
