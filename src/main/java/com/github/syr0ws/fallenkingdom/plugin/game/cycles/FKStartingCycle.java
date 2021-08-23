package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKWaitingListener;
import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.cycle.GameCycleTask;
import com.github.syr0ws.universe.sdk.game.cycle.types.StartingCycle;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
import com.github.syr0ws.universe.sdk.timer.TimerActionManager;
import com.github.syr0ws.universe.sdk.timer.TimerUtils;
import org.bukkit.configuration.ConfigurationSection;

public class FKStartingCycle extends StartingCycle {

    private final TimerActionManager actionManager;

    private GameCycleTask task;

    public FKStartingCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);
        this.actionManager = new TimerActionManager();
    }


    @Override
    public void load() {
        super.load();

        // Handling listeners.
        this.registerListeners();

        // Handling displays.
        this.loadActions();
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

    @Override
    public FKModel getModel() {
        return (FKModel) super.getModel();
    }

    @Override
    public FKController getController() {
        return (FKController) super.getController();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        manager.addListener(new FKWaitingListener(this.getModel(), this.getController()));
    }

    private void startTask() {

        FKSettings settings = this.getModel().getSettings();
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

                FKStartingCycle.this.actionManager.executeActions(this.duration);

                this.duration--;

            } else FKStartingCycle.this.done(); // Stopping cycle.
        }
    }
}
