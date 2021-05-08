package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.controller.SimpleGameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameModelCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class FallenKingdomPlugin extends JavaPlugin {

    private GameModel model;
    private GameController controller;

    @Override
    public void onEnable() {

        this.loadConfiguration();

        this.model = this.createModel();
        this.controller = this.createController(this.model);

        this.registerCommands();
    }

    private void loadConfiguration() {
        super.saveDefaultConfig();
    }

    private void registerCommands() {
        super.getCommand("fallenkingdom").setExecutor(new CommandFK(this, this.model, this.controller));
    }

    private GameModel createModel() {
        GameModelCreator creator = new GameModelCreator(this);
        return creator.getModel();
    }

    private GameController createController(GameModel model) {
        return new SimpleGameController(this, model);
    }
}
