package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.listeners.*;
import com.github.syr0ws.fallenkingdom.plugin.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.plugin.notifiers.EndNotifier;
import com.github.syr0ws.fallenkingdom.plugin.notifiers.NetherNotifier;
import com.github.syr0ws.fallenkingdom.plugin.notifiers.PvPNotifier;
import com.github.syr0ws.universe.api.attributes.AttributeObserver;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.api.settings.Setting;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.displays.DisplayUtils;
import com.github.syr0ws.universe.sdk.game.cycle.GameCycleTask;
import com.github.syr0ws.universe.sdk.game.cycle.types.RunningCycle;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
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

        manager.addListener(new FKGameListener(this.getModel()));
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
        this.notifiers.add(new NetherNotifier(model, this.manager));
        this.notifiers.add(new EndNotifier(model, this.manager));

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
        Setting<Integer> netherActivationTimeSetting = accessor.getNetherActivationTimeSetting();
        Setting<Integer> endActivationTimeSetting = accessor.getEndActivationTimeSetting();
        Setting<Integer> maxDurationSetting = accessor.getMaxGameDurationSetting();

        Setting<Boolean> allowNetherSetting = accessor.getAllowNetherSetting();
        Setting<Boolean> allowEndSetting = accessor.getAllowEndSetting();

        // Setting actions.
        actionManager.addAction(pvpSetting.getValue(), () -> model.setPvPEnabled(true));
        actionManager.addAction(assaultsSetting.getValue(), () -> model.setAssaultsEnabled(true));
        actionManager.addAction(maxDurationSetting.getValue(), this::done);

        if(allowNetherSetting.getValue())
            actionManager.addAction(netherActivationTimeSetting.getValue(), () -> model.setNetherEnabled(true));

        if(allowEndSetting.getValue())
            actionManager.addAction(endActivationTimeSetting.getValue(), () -> model.setEndEnabled(true));

        // Adding display actions.
        TimerUtils.loadDisplayActions(this.actionManager, this.getGame().getLangService(), this.getCycleSection());
    }

    private void loadDisplays() {

        for (GameDisplayEnum value : GameDisplayEnum.values())
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
