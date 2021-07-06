package com.github.syr0ws.fallenkingdom.game.model.cycles;

import com.github.syr0ws.fallenkingdom.FKGame;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.game.model.settings.SettingAccessor;
import com.github.syr0ws.fallenkingdom.listeners.WaitingCycleListener;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.attributes.Attribute;
import com.github.syr0ws.universe.attributes.AttributeObserver;
import com.github.syr0ws.universe.game.model.cycle.GameCycle;
import com.github.syr0ws.universe.game.model.cycle.GameCycleTask;
import com.github.syr0ws.universe.listeners.ListenerManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Collections;

public class GameWaitingCycle extends GameCycle implements AttributeObserver {

    private final FKController controller;
    private final FKModel model;

    private GameCycleTask task;

    public GameWaitingCycle(FKGame game, FKController controller, FKModel model) {
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

        // Adding observer.
        this.model.addObserver(this);
    }

    @Override
    public void unload() {
        super.unload();

        // Unregistering listeners.
        super.getListenerManager().removeListeners();

        // Removing observer.
        this.model.removeObserver(this);
    }

    @Override
    public void stop() {
        super.stop();

        // Stopping current task.
        this.stopTask();
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        manager.addListener(new WaitingCycleListener(this.controller, this.model));
    }

    private void startTask() {

        SettingAccessor settings = GameWaitingCycle.this.model.getSettings();
        int duration = settings.getStartingCycleDurationSetting().getValue();

        this.task = new WaitingCycleTask(this.getGame(), duration);
        this.task.start();
    }

    private void stopTask() {
        this.task.cancel();
        this.task = null; // Avoid reuse.
    }

    private ConfigurationSection getCycleSection() {
        return super.getGame().getConfig().getConfigurationSection("waiting-cycle");
    }

    @Override
    public void onUpdate(Attribute attribute) {

        GameState state = this.model.getState();

        if(state == GameState.WAITING) this.stopTask();
        else if(state == GameState.STARTING) this.startTask();
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.STATE_CHANGE);
    }

    private class WaitingCycleTask extends GameCycleTask {

        private int duration;

        public WaitingCycleTask(Game game, int duration) {
            super(game);

            if(duration < 0)
                throw new IllegalArgumentException("Duration must be positive.");

            this.duration = duration;
        }

        @Override
        public void run() {

            if(this.duration < 0) {

                GameWaitingCycle.this.stop(); // Stopping cycle.

            } else this.duration--;
        }
    }
}
