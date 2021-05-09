package com.github.syr0ws.fallenkingdom.commands;

import com.github.syr0ws.fallenkingdom.display.types.Message;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
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
            new Message(section.getString("no-permission")).displayTo(sender);
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

    /*
        Command : /fk start
     */
    private void onStartCommand(CommandSender sender, ConfigurationSection section) {

        ConfigurationSection startSection = section.getConfigurationSection("start");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_START.get())) {
            new Message(startSection.getString("no-permission")).displayTo(sender);
            return;
        }

        // TODO Check that no game is running.

        this.controller.startGame();
    }

    /*
        Command : /fk stop
     */
    private void onStopCommand(CommandSender sender, ConfigurationSection section) {

        ConfigurationSection stopSection = section.getConfigurationSection("stop");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_STOP.get())) {
            new Message(stopSection.getString("no-permission")).displayTo(sender);
            return;
        }

        // TODO Check that a game is running.

        this.controller.stopGame();
    }

    /*
        Command : /fk pvp on|off
     */

    // Command : /fk pvp
    private void onPvPCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection pvpSection = section.getConfigurationSection("pvp");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_PVP.get())) {
            new Message(pvpSection.getString("no-permission")).displayTo(sender);
            return;
        }

        // Only enable / disable pvp when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new Message(pvpSection.getString("game-not-running")).displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("on")) {

            this.onPvPOnCommand(sender, pvpSection);

        } else if(args[1].equalsIgnoreCase("off")) {

            this.onPvPOffCommand(sender, pvpSection);

        } else ; // TODO Send usages here.
    }

    // Command : /fk pvp on
    private void onPvPOnCommand(CommandSender sender, ConfigurationSection section) {

        if(this.model.isPvPEnabled()) {

            String message = section.getString("already-enabled");
            new Message(message).displayTo(sender);

        } else this.model.setPvPEnabled(true);
    }

    // Command : /fk pvp off
    private void onPvPOffCommand(CommandSender sender, ConfigurationSection section) {

        if(!this.model.isPvPEnabled()) {

            String message = section.getString("already-disabled");
            new Message(message).displayTo(sender);

        } else this.model.setPvPEnabled(false);
    }

    /*
        Command : /fk assaults on|off
     */

    // Command : /fk assaults
    private void onAssaultsCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection assaultsSection = section.getConfigurationSection("assaults");

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_ASSAULTS.get())) {
            new Message(assaultsSection.getString("no-permission")).displayTo(sender);
            return;
        }

        // Only enable / disable assaults when the game is running.
        if(this.model.getState() != GameState.RUNNING) {
            new Message(assaultsSection.getString("game-not-running")).displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("on")) {

            this.onAssaultsOnCommand(sender, assaultsSection);

        } else if(args[1].equalsIgnoreCase("off")) {

            this.onAssaultsOffCommand(sender, assaultsSection);

        } else ; // TODO Send usages here.
    }

    // Command : /fk assaults on
    private void onAssaultsOnCommand(CommandSender sender, ConfigurationSection section) {

        if(this.model.areAssaultsEnabled()) {

            String message = section.getString("already-enabled");
            new Message(message).displayTo(sender);

        } else this.model.setAssaultsEnabled(true);
    }

    // Command : /fk assaults off
    private void onAssaultsOffCommand(CommandSender sender, ConfigurationSection section) {

        if(!this.model.areAssaultsEnabled()) {

            String message = section.getString("already-disabled");
            new Message(message).displayTo(sender);

        } else this.model.setAssaultsEnabled(false);
    }
}
