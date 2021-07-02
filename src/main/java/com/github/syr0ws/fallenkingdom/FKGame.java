package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.game.controller.CraftFKController;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.v2.CraftFKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.v2.GameInitializer;
import com.github.syr0ws.fallenkingdom.game.model.v2.modes.PlayingMode;
import com.github.syr0ws.fallenkingdom.game.model.v2.modes.SpectatorMode;
import com.github.syr0ws.fallenkingdom.game.model.v2.modes.WaitingMode;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.game.model.mode.ModeFactory;
import com.github.syr0ws.universe.modules.ModuleEnum;
import com.github.syr0ws.universe.modules.ModuleService;

public class FKGame extends Game {

    private CraftFKModel model;
    private CraftFKController controller;

    @Override
    public void onEnable() {

        // Loading config.
        super.saveDefaultConfig();

        try {

            // Model setup.
            this.setupGameModel();

            // Controller setup.
            this.setupGameController();

            // Loading modules.
            this.loadModules();

            // Registering game modes.
            this.registerGameModes();

            // Adding notifiers.
            this.setupNotifiers();

        } catch (GameException e) { e.printStackTrace(); }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public FKModel getGameModel() {
        return this.model;
    }

    @Override
    public FKController getGameController() {
        return this.controller;
    }

    private void setupGameModel() throws GameException {
        GameInitializer initializer = new GameInitializer(this);
        this.model = initializer.getGame();
    }

    private void setupGameController() {
        this.controller = new CraftFKController(this, this.model);
    }

    private void registerGameModes() {
        ModeFactory.registerMode(new WaitingMode(this));
        ModeFactory.registerMode(new PlayingMode(this));
        ModeFactory.registerMode(new SpectatorMode(this));
    }

    private void loadModules() {

        ModuleService service = super.getModuleService();
        service.enableModule(ModuleEnum.COMBAT_MODULE.newInstance(this));
    }

    private void setupNotifiers() {
        this.model.addObserver(new PvPNotifier(this.model, this));
        this.model.addObserver(new AssaultsNotifier(this.model, this));
    }
}
