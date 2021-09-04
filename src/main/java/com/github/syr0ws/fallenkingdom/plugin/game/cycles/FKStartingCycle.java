package com.github.syr0ws.fallenkingdom.plugin.game.cycles;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.fallenkingdom.plugin.FKGame;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKWaitingListener;
import com.github.syr0ws.universe.api.game.controller.GameController;
import com.github.syr0ws.universe.api.game.model.GameModel;
import com.github.syr0ws.universe.api.services.GameServicesManager;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.controller.cycle.GameCycleTask;
import com.github.syr0ws.universe.sdk.game.controller.cycle.types.GameStartingCycle;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.timer.TimerActionManager;
import com.github.syr0ws.universe.sdk.timer.TimerUtils;
import org.bukkit.configuration.ConfigurationSection;

public class FKStartingCycle extends GameStartingCycle {

    private final TimerActionManager actionManager;

    private GameCycleTask task;

    public FKStartingCycle(Game game, GameModel model, GameController controller) {
        super(game, model, controller);
        this.actionManager = new TimerActionManager();
    }

    @Override
    public void registerListeners(ListenerManager manager) {
        super.registerListeners(manager);
        manager.addListener(new FKWaitingListener(this.getModel(), this.getController()));
    }

    @Override
    public void enable() {

        this.loadActions(); // Handling displays.
        this.startTask(); // Starting task.

        super.enable();
    }

    @Override
    public void disable() {

        this.stopTask(); // Stopping task.

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

        GameServicesManager manager = this.getGame().getServicesManager();
        LangService service = manager.getProvider(LangService.class);

        TimerUtils.loadDisplayActions(this.actionManager, service, this.getCycleSection());
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
