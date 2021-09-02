package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKBlockListener;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKEliminationListener;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKGameListener;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKPlayerListener;
import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.api.settings.Setting;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.controller.cycle.GameCycleTask;
import com.github.syr0ws.universe.sdk.game.controller.cycle.types.GameRunningCycle;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
import com.github.syr0ws.universe.sdk.timer.TimerActionManager;
import com.github.syr0ws.universe.sdk.timer.TimerUtils;
import com.github.syr0ws.universe.sdk.tools.Task;
import org.bukkit.configuration.ConfigurationSection;

public class FKRunningCycle extends GameRunningCycle {
    ;
    private final TimerActionManager actionManager;

    private Task task;

    public FKRunningCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);

        this.actionManager = new TimerActionManager();
    }

    private void load() {

        // Registering listeners.
        this.registerListeners();

        // Handling captures.
        this.getController().getCaptureManager().enable();

        // Loading actions.
        this.loadActions();
    }

    private void unload() {

        // Handling captures.
        this.getController().getCaptureManager().disable();
    }

    @Override
    public void enable() {

        // Loading the cycle.
        this.load();

        // Starting task.
        this.startTask();

        super.enable();
    }

    @Override
    public void disable() {

        // Unloading the cycle.
        this.unload();

        // Stopping task.
        this.stopTask();

        super.disable();
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
