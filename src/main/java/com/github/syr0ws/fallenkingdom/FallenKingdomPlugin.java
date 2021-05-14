package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.GameException;
import com.github.syr0ws.fallenkingdom.game.controller.FKGameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import org.bukkit.plugin.java.JavaPlugin;

public class FallenKingdomPlugin extends JavaPlugin {

    private FKGameController controller;

    @Override
    public void onEnable() {

        this.loadConfiguration();

        this.controller = this.createController();

        // TODO To change by disabling plugin when an error occurred.
        try { this.controller.initGame();
        } catch (GameException e) { e.printStackTrace(); }

        this.setupNotifiers();
        this.registerCommands();
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

    private FKGameController createController() {
        return new FKGameController(this);
    }

    public FKGameController getGameController() {
        return this.controller;
    }
}
