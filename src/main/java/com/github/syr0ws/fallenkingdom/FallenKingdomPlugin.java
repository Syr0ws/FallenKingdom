package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandAssaults;
import com.github.syr0ws.fallenkingdom.commands.CommandPvP;
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
        super.getCommand("pvp").setExecutor(new CommandPvP(this, this.controller.getModel()));
        super.getCommand("assaults").setExecutor(new CommandAssaults(this, this.controller.getModel()));
    }

    private void initGame() {

        SimpleGameController controller = new SimpleGameController(this);
        controller.init();

        this.controller = controller;
    }
}
