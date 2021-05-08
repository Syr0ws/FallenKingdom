package com.github.syr0ws.fallenkingdom.commands;

import com.github.syr0ws.fallenkingdom.game.controller.GameController;
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

public class CommandFK implements CommandExecutor {

    private final Plugin plugin;
    private final GameModel model;
    private final GameController controller;

    public CommandFK(Plugin plugin, GameModel model, GameController controller) {
        this.plugin = plugin;
        this.model = model;
        this.controller = controller;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("command-fk");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK.get())) {
            new SimpleMessage(section.getString("no-permission")).send(sender);
            return true;
        }

        if(args.length == 1) {

            if(args[0].equalsIgnoreCase("start")) {

                this.onStartCommand(sender, section);

            } else if(args[0].equalsIgnoreCase("stop")) {

                this.onStopCommand(sender, section);
            }

        } else if(args.length == 2) {

            if(args[0].equalsIgnoreCase("pvp")) {

                this.onPvPCommand(sender, section, args);

            } else if(args[0].equalsIgnoreCase("assaults")) {

                this.onAssaultsCommand(sender, section, args);
            }

        } else ; // TODO send usages here.

        return true;
    }

    private void onStartCommand(CommandSender sender, ConfigurationSection section) {

        ConfigurationSection startSection = section.getConfigurationSection("start");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_START.get())) {
            new SimpleMessage(startSection.getString("no-permission")).send(sender);
            return;
        }

        this.controller.startGame();
    }

    private void onStopCommand(CommandSender sender, ConfigurationSection section) {

        ConfigurationSection stopSection = section.getConfigurationSection("stop");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_STOP.get())) {
            new SimpleMessage(stopSection.getString("no-permission")).send(sender);
            return;
        }

        this.controller.stopGame();
    }

    private void onPvPCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection pvpSection = section.getConfigurationSection("pvp");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_PVP.get())) {
            new SimpleMessage(pvpSection.getString("no-permission")).send(sender);
            return;
        }

        // Only enable / disable pvp when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new SimpleMessage(pvpSection.getString("game-not-running")).send(sender);
            return;
        }

        if(args[0].equalsIgnoreCase("on")) {

            // Command : /pvp on
            if(this.model.isPvPEnabled()) {

                String message = pvpSection.getString("already-enabled");
                new SimpleMessage(message).send(sender);

            } else this.model.setPvPEnabled(true);

        } else if(args[0].equalsIgnoreCase("off")) {

            // Command : /pvp off
            if(!this.model.isPvPEnabled()) {

                String message = pvpSection.getString("already-disabled");
                new SimpleMessage(message).send(sender);

            } else this.model.setPvPEnabled(false);

        } else ; // TODO Send usages here.
    }

    private void onAssaultsCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection assaultsSection = section.getConfigurationSection("assaults");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_ASSAULTS.get())) {
            new SimpleMessage(assaultsSection.getString("no-permission")).send(sender);
            return;
        }

        // Only enable / disable assaults when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new SimpleMessage(assaultsSection.getString("game-not-running")).send(sender);
            return;
        }

        if(args[0].equalsIgnoreCase("on")) {

            // Command : /assaults on
            if(this.model.areAssaultsEnabled()) {

                String message = assaultsSection.getString("already-enabled");
                new SimpleMessage(message).send(sender);

            } else this.model.setAssaultsEnabled(true);

        } else if(args[0].equalsIgnoreCase("off")) {

            if(!this.model.areAssaultsEnabled()) {

                String message = assaultsSection.getString("already-disabled");
                new SimpleMessage(message).send(sender);

            } else this.model.setAssaultsEnabled(false);

        } else ; // TODO Send usages here.
    }
}
