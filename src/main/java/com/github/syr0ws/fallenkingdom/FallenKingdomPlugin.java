package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.commands.CommandAssaults;
import com.github.syr0ws.fallenkingdom.commands.CommandPvP;
import org.bukkit.plugin.java.JavaPlugin;

public class FallenKingdomPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        this.loadConfiguration();
        this.registerCommands();

    }

    private void loadConfiguration() {
        super.saveDefaultConfig();
    }

    private void registerCommands() {
        super.getCommand("pvp").setExecutor(new CommandPvP(this, null)); // TODO to change
        super.getCommand("assaults").setExecutor(new CommandAssaults(this, null)); // TODO to change
    }
}
