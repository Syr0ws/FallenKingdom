package com.github.syr0ws.fallenkingdom.game.model.cycles.impl;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.cycles.GameCycle;
import com.github.syr0ws.fallenkingdom.game.model.cycles.listeners.GameWaitingListener;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.scoreboards.ScoreboardManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.dao.TimerDisplayDAO;
import com.github.syr0ws.universe.settings.Setting;
import com.github.syr0ws.universe.tools.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GameWaitingCycle extends GameCycle implements AttributeObserver {

    private final GameModel game;
    private final ScoreboardManager sbManager = new ScoreboardManager();

    private CycleTask task;

    public GameWaitingCycle(Plugin plugin, GameModel game) {
        super(plugin);
        this.game = game;
    }

    @Override
    public void load() {

        this.loadDisplays();

        ListenerManager listenerManager = super.getListenerManager();
        listenerManager.addListener(new GameWaitingListener(this, this.sbManager));

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
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.STATE_CHANGE);
    }

    public void startTask() {

        SettingAccessor accessor = this.game.getSettings();
        Setting<Integer> setting = accessor.getStartingCycleDurationSetting();

        this.task = new CycleTask(setting.getValue());
        this.task.start();
    }

    public void stopTask() {

        if(this.task == null || !this.task.isRunning()) return;

        this.task.stop();
        this.task = null;
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
}
