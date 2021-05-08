package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.controller.SimpleGameController;
import org.bukkit.plugin.java.JavaPlugin;

public class FallenKingdomPlugin extends JavaPlugin {

    private GameController controller;

    @Override
    public void onEnable() {

        this.loadConfiguration();
        this.initGame();
        this.registerCommands();
    }

    private void loadConfiguration() {
        super.saveDefaultConfig();
    }

    private void registerCommands() {
        super.getCommand("fallenkingdom").setExecutor(new CommandFK(this, this.controller.getModel(), this.controller));
    }

    private void initGame() {

        SimpleGameController controller = new SimpleGameController(this);
        controller.init();

        this.controller = controller;
    }
}
