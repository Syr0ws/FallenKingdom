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

public class CommandPvP implements CommandExecutor {

    private final Plugin plugin;
    private final GameModel model;

    public CommandPvP(Plugin plugin, GameModel model) {
        this.plugin = plugin;
        this.model = model;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("command-pvp");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_PVP.get())) {
            new SimpleMessage(section.getString("no-permission")).send(sender);
            return true;
        }

        // Only enable / disable pvp when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new SimpleMessage(section.getString("game-not-running")).send(sender);
            return true;
        }

        if(args[0].equalsIgnoreCase("on")) {

            // Command : /pvp on
            this.enablePvP(sender, section);

        } else if(args[0].equalsIgnoreCase("off")) {

            // Command : /pvp off
            this.disablePvP(sender, section);

        } else ; // TODO Send usages here.

        return true;
    }

    private void enablePvP(CommandSender sender, ConfigurationSection section) {

        if(this.model.isPvPEnabled()) {

            String message = section.getString("already-enabled");
            new SimpleMessage(message).send(sender);

        } else this.model.setPvPEnabled(true);
    }

    private void disablePvP(CommandSender sender, ConfigurationSection section) {

        if(!this.model.isPvPEnabled()) {

            String message = section.getString("already-disabled");
            new SimpleMessage(message).send(sender);

        } else this.model.setPvPEnabled(false);
    }
}
