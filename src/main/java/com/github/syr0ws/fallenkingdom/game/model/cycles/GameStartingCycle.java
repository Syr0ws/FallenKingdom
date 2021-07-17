package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.WaitingCycleListener;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.TimerUtils;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.displays.DisplayManager;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleTask;
import com.github.syr0ws.universe.listeners.ListenerManager;
import org.bukkit.configuration.ConfigurationSection;

public class GameStartingCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    private final TimerActionManager actionManager;

    private DisplayManager manager;
    private GameCycleTask task;

    public GameStartingCycle(FKGame game, FKController controller, FKModel model) {
        super(game);

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.controller = controller;
        this.model = model;

        this.actionManager = new TimerActionManager();
    }

    @Override
    public void load() {
        super.load();

        // Registering cycle listeners.
        this.registerListeners();

        // Handling displays.
        this.loadActions();
    }

    @Override
    public void unload() {
        super.unload();

        // Unregistering listeners.
        super.getListenerManager().removeListeners();
    }

    @Override
    public void start() {
        super.start();

        // Starting task.
        this.startTask();
    }

    @Override
    public void stop() {
        super.stop();

        // Stopping task.
        this.stopTask();
    }

    @Override
    public FKGame getGame() {
        return (FKGame) super.getGame();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        manager.addListener(new WaitingCycleListener(this.controller, this.model));
    }

    private void startTask() {

        SettingAccessor settings = this.model.getSettings();
        int duration = settings.getStartingCycleDurationSetting().getValue();

        this.task = new StartingCycleTask(this.getGame(), duration);
        this.task.start();
    }

    private void stopTask() {
        this.task.cancel();
        this.task = null; // Avoid reuse.
    }

    private void loadActions() {
        TimerUtils.loadDisplayActions(this.actionManager, this.getGame().getLangService(), this.getCycleSection());
    }

    private ConfigurationSection getCycleSection() {
        return super.getGame().getConfig().getConfigurationSection("waiting-cycle");
    }

    private class StartingCycleTask extends GameCycleTask {

        private int duration;

        public StartingCycleTask(Game game, int duration) {
            super(game);

            if(duration < 0)
                throw new IllegalArgumentException("Duration must be positive.");

            this.duration = duration;
        }

        @Override
        public void run() {

            if(this.duration >= 0) {

                GameStartingCycle.this.actionManager.executeActions(this.duration);

                this.duration--;

            } else GameStartingCycle.this.done(); // Stopping cycle.
        }
    }
}
