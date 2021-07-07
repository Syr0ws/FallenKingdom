package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.GameBlockListener;
import com.github.syr0ws.fallenkingdom.listeners.GamePlayerListener;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleTask;
import com.github.syr0ws.universe.listeners.ListenerManager;
import com.github.syr0ws.universe.tools.Task;

public class GameRunningCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    private Task task;

    public GameRunningCycle(FKGame game, FKController controller, FKModel model) {
        super(game);

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        this.controller = controller;
        this.model = model;
    }

    @Override
    public void load() {
        super.load();

        // Registering cycle listeners.
        this.registerListeners();

        // Handling captures.
        this.controller.getCaptureManager().enable();
    }

    @Override
    public void unload() {
        super.unload();

        // Unregistering listeners.
        super.getListenerManager().removeListeners();

        // Handling captures.
        this.controller.getCaptureManager().disable();
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

        manager.addListener(new GamePlayerListener(this.getGame()));
        manager.addListener(new GameBlockListener(this.getGame()));
    }

    private void startTask() {

        SettingAccessor settings = this.model.getSettings();
        int duration = settings.getMaxGameDurationSetting().getValue();

        this.task = new RunningCycleTask(this.getGame(), duration);
        this.task.start();
    }

    private void stopTask() {
        this.task.cancel();
        this.task = null; // Avoid reuse.
    }

    private class RunningCycleTask extends GameCycleTask {

        private final int duration;

        public RunningCycleTask(Game game, int duration) {
            super(game);

            if(duration < 0)
                throw new IllegalArgumentException("Duration must be positive.");

            this.duration = duration;
        }

        @Override
        public void run() {

            if(GameRunningCycle.this.model.getTime() > this.duration) {

                GameRunningCycle.this.done();

            } else GameRunningCycle.this.model.addTime();
        }
    }
}
