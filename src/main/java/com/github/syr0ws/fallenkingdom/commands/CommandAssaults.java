package com.github.syr0ws.fallenkingdom.commands;

import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.messages.types.SimpleMessage;
import com.github.syr0ws.fallenkingdom.tools.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class CommandAssaults implements CommandExecutor {

    private final Plugin plugin;
    private final GameModel model;

    public CommandAssaults(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("command-assaults");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_ASSAULTS.get())) {
            new SimpleMessage(section.getString("no-permission")).send(sender);
            return true;
        }

        // Only enable / disable assaults when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new SimpleMessage(section.getString("game-not-running")).send(sender);
            return true;
        }

        if(args[0].equalsIgnoreCase("on")) {

            // Command : /assaults on
            this.enableAssaults(sender, section);

        } else if(args[0].equalsIgnoreCase("off")) {

            // Command : /assaults off
            this.disableAssaults(sender, section);

        } else ; // TODO Send usages here.

        return true;
    }

    private void enableAssaults(CommandSender sender, ConfigurationSection section) {

        if(this.model.areAssaultsEnabled()) {

            String message = section.getString("already-enabled");
            new SimpleMessage(message).send(sender);

        } else this.model.setAssaultsEnabled(true);
    }

    private void disableAssaults(CommandSender sender, ConfigurationSection section) {

        if(!this.model.areAssaultsEnabled()) {

            String message = section.getString("already-disabled");
            new SimpleMessage(message).send(sender);

        } else this.model.setAssaultsEnabled(false);
    }
}
