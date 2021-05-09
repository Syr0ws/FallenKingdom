package com.github.syr0ws.fallenkingdom.commands;

import com.github.syr0ws.fallenkingdom.display.placeholders.GlobalPlaceholder;
import com.github.syr0ws.fallenkingdom.display.placeholders.TeamPlaceholder;
import com.github.syr0ws.fallenkingdom.display.types.Message;
import com.github.syr0ws.fallenkingdom.game.controller.GameController;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.GameState;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.tools.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

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

        // TODO Handle args in methods.

        if(args.length == 0) {
            // TODO send help here.
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                this.onStartCommand(sender, section, args);
                break;
            case "stop":
                this.onStopCommand(sender, section, args);
                break;
            case "pvp":
                this.onPvPCommand(sender, section, args);
                break;
            case "assaults":
                this.onAssaultsCommand(sender, section, args);
                break;
            case "team":
                this.onTeamCommand(sender, section, args);
                break;
            default:
                // TODO send help here.
                break;
        }

        return true;
    }

    /*
        Command : /fk start
    */
    private void onStartCommand(CommandSender sender, ConfigurationSection section, String[] args) {

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
    private void onStopCommand(CommandSender sender, ConfigurationSection section, String[] args) {

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

        if(args.length < 2) {
            // TODO send help here.
            return;
        }

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

        if(args.length != 2) {
            // TODO send help here.
            return;
        }

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

    /*
        Command : /fk team
    */

    // Command : /fk team
    private void onTeamCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection teamSection = section.getConfigurationSection("team");

        if(args.length < 2) {
            // TODO send help here.
            return;
        }

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_TEAM.get())) {
            new Message(teamSection.getString("no-permission")).displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("add")) {

            this.onTeamAddCommand(sender, teamSection, args);

        } else if(args[1].equalsIgnoreCase("remove")) {

            this.onTeamRemoveCommand(sender, teamSection, args);

        } else ; // TODO Send usages here.
    }

    // Command : /fk team add <player> <team>
    private void onTeamAddCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection addSection = section.getConfigurationSection("add");

        if(args.length != 4) {
            // TODO send help here.
            return;
        }

        Player target = Bukkit.getPlayerExact(args[2]);

        // Checking if the targeted player is valid.
        if(target == null) {
            new Message(section.getString("player-not-found")).displayTo(sender);
            return;
        }

        Optional<Team> optional = this.model.getTeamByName(args[3]);

        // Checking if the targeted team is valid.
        if(!optional.isPresent()) {
            new Message(section.getString("team-not-found")).displayTo(sender);
            return;
        }

        Team team = optional.get();

        if(team.contains(target)) {
            new Message(addSection.getString("already-in-team")).displayTo(sender);
            return;
        }

        // If player is already in another team, removing him from it.

        this.model.setTeam(target, team);

        Message message = new Message(addSection.getString("player-added"));
        message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, target.getName());
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());

        message.displayTo(sender);
    }

    // Command : /fk team remove <player>
    private void onTeamRemoveCommand(CommandSender sender, ConfigurationSection section, String[] args) {

        ConfigurationSection removeSection = section.getConfigurationSection("remove");

        if(args.length != 3) {
            // TODO send help here.
            return;
        }

        Player target = Bukkit.getPlayerExact(args[2]);

        // Checking if the targeted player is valid.
        if(target == null) {
            new Message(section.getString("player-not-found")).displayTo(sender);
            return;
        }

        Optional<Team> optional = this.model.getTeam(target);

        if(!optional.isPresent()) {
            new Message(removeSection.getString("not-in-team")).displayTo(sender);
            return;
        }

        Team team = optional.get();

        this.model.removeTeam(target);

        Message message = new Message(removeSection.getString("player-removed"));
        message.addPlaceholder(GlobalPlaceholder.PLAYER_NAME, target.getName());
        message.addPlaceholder(TeamPlaceholder.TEAM_NAME, team.getDisplayName());

        message.displayTo(sender);
    }
}
