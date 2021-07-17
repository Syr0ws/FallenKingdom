package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.cycles.displays.GameRunningDisplayEnum;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.GameBlockListener;
import com.github.syr0ws.fallenkingdom.listeners.GameEliminationListener;
import com.github.syr0ws.fallenkingdom.listeners.GamePlayerListener;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.TimerUtils;
import com.github.syr0ws.fallenkingdom.utils.DisplayUtils;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.displays.DisplayManager;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleTask;
import com.github.syr0ws.universe.listeners.ListenerManager;
import com.github.syr0ws.universe.settings.Setting;
import com.github.syr0ws.universe.tools.Task;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GameRunningCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    private final TimerActionManager actionManager;
    private final List<AttributeObserver> notifiers = new ArrayList<>();

    private DisplayManager manager;
    private Task task;

    public GameRunningCycle(FKGame game, FKController controller, FKModel model) {
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

        // Registering listeners.
        this.registerListeners();

        // Handling captures.
        this.controller.getCaptureManager().enable();

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

        // Unregistering listeners.
        super.getListenerManager().removeListeners();

        // Handling captures.
        this.controller.getCaptureManager().disable();

        // Removing notifiers.
        this.notifiers.forEach(this.model::removeObserver);
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
        manager.addListener(new GameEliminationListener(this.getGame()));
    }

    private void setupNotifiers() {

        // Storing notifiers to unregister them later.
        this.notifiers.add(new PvPNotifier(this.model, this.manager));
        this.notifiers.add(new AssaultsNotifier(this.model, this.manager));

        // Observing the model.
        this.notifiers.forEach(this.model::addObserver);
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

        TimerActionManager actionManager = this.actionManager;

        // Retrieving settings.
        SettingAccessor accessor = this.model.getSettings();

        Setting<Integer> pvpSetting = accessor.getPvPActivationTimeSetting();
        Setting<Integer> assaultsSetting = accessor.getAssaultsActivationTimeSetting();
        Setting<Integer> maxDurationSetting = accessor.getMaxGameDurationSetting();

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> this.model.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> this.model.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::done);

        // Adding display actions.
        TimerUtils.loadDisplayActions(this.actionManager, this.getCycleSection());
    }

    private void loadDisplays() {

        this.manager = DisplayUtils.getDisplayManager(this.getGame());

        for (GameRunningDisplayEnum value : GameRunningDisplayEnum.values())
            this.manager.loadDisplays(value.getPath());
    }

    private ConfigurationSection getCycleSection() {
        return super.getGame().getConfig().getConfigurationSection("running-cycle");
    }

    private class RunningCycleTask extends GameCycleTask {

        public RunningCycleTask(Game game) {
            super(game);
        }

        @Override
        public void run() {

            int time = GameRunningCycle.this.model.getTime();

            // Executing action for the current time.
            GameRunningCycle.this.actionManager.executeActions(time);

            // Removing the action because it will no longer be used.
            GameRunningCycle.this.actionManager.removeActions(time);

            // Adding time to the model.
            GameRunningCycle.this.model.addTime();
        }
    }
}
