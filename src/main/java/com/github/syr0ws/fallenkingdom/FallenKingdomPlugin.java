package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.controller.FKGameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.modules.ModuleEnum;
import com.github.syr0ws.universe.modules.ModuleService;
import org.bukkit.Bukkit;

public class FallenKingdomPlugin extends Game {

    private FKGameController controller;

    @Override
    public void onEnable() {

        this.loadConfiguration();

        boolean gameInitialized = this.initGame();

        if(!gameInitialized) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.setupNotifiers();
        this.registerCommands();

        // Loading modules.
        this.loadModules();
    }

    private void loadConfiguration() {
        super.saveDefaultConfig();
    }

    private void registerCommands() {
        super.getCommand("fallenkingdom").setExecutor(new CommandFK(this, controller.getGame(), this.controller));
    }

    private void setupNotifiers() {
        GameModel game = this.controller.getGame();
        game.addObserver(new PvPNotifier(game, this));
        game.addObserver(new AssaultsNotifier(game, this));
    }

    private void loadModules() {

        ModuleService service = super.getModuleService();
        service.enableModule(ModuleEnum.COMBAT_MODULE.newInstance(this));
    }

    private FKGameController createController() {
        return new FKGameController(this);
    }

    private boolean initGame() {

        this.controller = this.createController();

        boolean init = false;

        try {

            this.controller.init();
            init = true;

        } catch (GameException e) { e.printStackTrace(); }

        return init;
    }

    public FKGameController getGameController() {
        return this.controller;
    }
}
