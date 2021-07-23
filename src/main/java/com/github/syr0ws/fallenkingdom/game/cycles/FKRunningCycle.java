package com.github.syr0ws.fallenkingdom.game.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.cycles.displays.GameRunningDisplayEnum;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.FKSettings;
import com.github.syr0ws.fallenkingdom.listeners.FKBlockListener;
import com.github.syr0ws.fallenkingdom.listeners.FKEliminationListener;
import com.github.syr0ws.fallenkingdom.listeners.FKPlayerListener;
import com.github.syr0ws.fallenkingdom.listeners.FKTeamWinListener;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import com.github.syr0ws.universe.commons.cycle.types.RunningCycle;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.attributes.AttributeObserver;
import com.github.syr0ws.universe.sdk.displays.DisplayManager;
import com.github.syr0ws.universe.sdk.displays.DisplayUtils;
import com.github.syr0ws.universe.sdk.game.controller.GameController;
import com.github.syr0ws.universe.sdk.game.cycle.GameCycleTask;
import com.github.syr0ws.universe.sdk.game.model.GameModel;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
import com.github.syr0ws.universe.sdk.settings.Setting;
import com.github.syr0ws.universe.sdk.timer.TimerActionManager;
import com.github.syr0ws.universe.sdk.timer.TimerUtils;
import com.github.syr0ws.universe.sdk.tools.Task;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class FKRunningCycle extends RunningCycle {

    private final DisplayManager manager;
    private final TimerActionManager actionManager;
    private final List<AttributeObserver> notifiers = new ArrayList<>();

    private Task task;

    public FKRunningCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);

        this.manager = DisplayUtils.getDisplayManager(this.getGame());
        this.actionManager = new TimerActionManager();
    }


    @Override
    public void load() {
        super.load();

        // Registering listeners.
        this.registerListeners();

        // Handling captures.
        this.getController().getCaptureManager().enable();

        // Loading actions.
        this.loadActions();

        // Loading displays.
        this.loadDisplays();

        // Adding notifiers.
        this.setupNotifiers();
    }

    @Override
    public void unload() {
        super.unload();

        // Handling captures.
        this.getController().getCaptureManager().disable();

        // Removing notifiers.
        this.notifiers.forEach(this.getModel()::removeObserver);
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

        manager.addListener(new FKPlayerListener(this.getGame()));
        manager.addListener(new FKBlockListener(this.getGame()));
        manager.addListener(new FKEliminationListener(this.getGame()));
        manager.addListener(new FKTeamWinListener(this.manager));
    }

    private void setupNotifiers() {

        FKModel model = this.getModel();

        // Storing notifiers to unregister them later.
        this.notifiers.add(new PvPNotifier(model, this.manager));
        this.notifiers.add(new AssaultsNotifier(model, this.manager));

        // Observing the model.
        this.notifiers.forEach(model::addObserver);
    }

    private void startTask() {
        this.task = new RunningCycleTask(this.getGame());
        this.task.start();
    }

    private void stopTask() {
        this.task.cancel();
        this.task = null; // Avoid reuse.
    }

    private void loadActions() {

        FKModel model = this.getModel();

        TimerActionManager actionManager = this.actionManager;

        // Retrieving settings.
        FKSettings accessor = model.getSettings();

        Setting<Integer> pvpSetting = accessor.getPvPActivationTimeSetting();
        Setting<Integer> assaultsSetting = accessor.getAssaultsActivationTimeSetting();
        Setting<Integer> maxDurationSetting = accessor.getMaxGameDurationSetting();

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> model.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> model.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::done);

        // Adding display actions.
        TimerUtils.loadDisplayActions(this.actionManager, this.getGame().getLangService(), this.getCycleSection());
    }

    private void loadDisplays() {

        for (GameRunningDisplayEnum value : GameRunningDisplayEnum.values())
            this.manager.loadDisplays(value.getPath());
    }

    private ConfigurationSection getCycleSection() {
        return super.getGame().getConfig().getConfigurationSection("game-cycle");
    }

    private class RunningCycleTask extends GameCycleTask {

        public RunningCycleTask(Game game) {
            super(game);
        }

        @Override
        public void run() {

            FKModel model = FKRunningCycle.this.getModel();

            int time = model.getTime();

            // Executing action for the current time.
            FKRunningCycle.this.actionManager.executeActions(time);

            // Removing the action because it will no longer be used.
            FKRunningCycle.this.actionManager.removeActions(time);

            // Adding time to the model.
            model.addTime();
        }
    }
}
