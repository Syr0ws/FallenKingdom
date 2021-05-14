package com.github.syr0ws.fallenkingdom.game.model.cycle.types;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.listeners.ListenerManager;
import com.github.syr0ws.fallenkingdom.listeners.PreRunningListener;
import com.github.syr0ws.fallenkingdom.tools.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class StartingCycle extends GameCycle {

    private final Plugin plugin;
    private final GameModel game;
    private final ListenerManager manager;
    private CycleTask task;

    public StartingCycle(Plugin plugin, GameModel game) {
        this.plugin = plugin;
        this.game = game;
        this.manager = new ListenerManager(plugin);
    }

    @Override
    public void start() {
        this.manager.addListener(new PreRunningListener(this.plugin, this.game));
        this.startTask();
    }

    @Override
    public void stop() {
        this.manager.removeListeners();
        this.stopTask();
    }

    @Override
    public GameState getState() {
        return GameState.STARTING;
    }

    private void startTask() {

        ConfigurationSection section = this.getCycleSection();
        ConfigurationSection taskSection = section.getConfigurationSection("timer");

        int duration = taskSection.getInt("duration");

        this.task = new CycleTask(duration);
        this.task.start();
    }

    private void stopTask() {
        this.task.stop();
        this.task = null;
    }

    private ConfigurationSection getCycleSection() {
        FileConfiguration config = this.plugin.getConfig();
        return config.getConfigurationSection("game-starting");
    }

    private class CycleTask extends Task {

        private int duration;

        public CycleTask(int duration) {
            this.duration = duration;
        }

        @Override
        public void run() {

            if(this.duration >= 0) {

                this.duration--;

            } else StartingCycle.this.finish(); // Setting the cycle as finished.
        }

        @Override
        public void start() {
            super.start();
            this.runTaskTimer(StartingCycle.this.plugin, 0L, 20L);
        }
    }
}
