package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.GameBlockListener;
import com.github.syr0ws.fallenkingdom.listeners.GamePlayerListener;
import com.github.syr0ws.fallenkingdom.timer.TimerActionManager;
import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.dao.TimerDisplayDAO;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleTask;
import com.github.syr0ws.universe.listeners.ListenerManager;
import com.github.syr0ws.universe.settings.Setting;
import com.github.syr0ws.universe.tools.Task;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Map;

public class GameRunningCycle extends GameCycle {

    private final FKController controller;
    private final FKModel model;

    private final TimerActionManager actionManager;
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

        // Registering cycle listeners.
        this.registerListeners();

        // Handling captures.
        this.controller.getCaptureManager().enable();

        // Handling actions and displays.
        this.loadActions();
        this.loadDisplays();
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
    }

    private void loadDisplays() {

        ConfigurationSection section = this.getCycleSection();

        TimerDisplayDAO dao = new TimerDisplayDAO(section);

        try {

            Map<Integer, Collection<Display>> displays = dao.getTimeDisplays("displays");

            displays.forEach((time, list) -> list.stream()
                    .map(DisplayAction::new)
                    .forEach(action -> this.actionManager.addAction(time, action)));

        } catch (DisplayException e) { e.printStackTrace(); }
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

            GameRunningCycle.this.actionManager.executeActions(time);
            GameRunningCycle.this.model.addTime();
        }
    }
}
