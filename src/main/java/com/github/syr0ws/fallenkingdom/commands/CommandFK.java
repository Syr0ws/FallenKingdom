package com.github.syr0ws.fallenkingdom.commands;

import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKPlayer;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.fallenkingdom.tools.Permission;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.commons.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.sdk.displays.types.Message;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandFK implements CommandExecutor {

    private final FKModel model;
    private final FKController controller;
    private final LangService service;

    public CommandFK(FKModel model, FKController controller, LangService service) {

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.model = model;
        this.controller = controller;
        this.service = service;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK.get())) {
            this.getMessage("no-permission").displayTo(sender);
            return true;
        }

        if(args.length == 0) {
            // TODO send help here.
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                this.onStartCommand(sender);
                break;
            case "stop":
                this.onStopCommand(sender);
                break;
            case "pvp":
                this.onPvPCommand(sender, args);
                break;
            case "assaults":
                this.onAssaultsCommand(sender, args);
                break;
            case "team":
                this.onTeamCommand(sender, args);
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
    private void onStartCommand(CommandSender sender) {

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_START.get())) {
            this.getMessage("start.no-permission").displayTo(sender);
            return;
        }

        // Checking if there is a started game.
        if(!this.model.isWaiting()) {
            this.getMessage("start.already-started").displayTo(sender);
            return;
        }

        try {

            this.controller.startGame();

        } catch (GameException e) {

            e.printStackTrace();

            this.getMessage("start.error").displayTo(sender);
        }
    }

    /*
        Command : /fk stop
    */
    private void onStopCommand(CommandSender sender) {

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_STOP.get())) {
            this.getMessage("stop.no-permission").displayTo(sender);
            return;
        }

        // Checking if there is a started game.
        if(!this.model.isStarting() && !this.model.isStarted()) {
            this.getMessage("stop.not-started").displayTo(sender);
            return;
        }

        // TODO Check if the game is already finished.

        try {

            this.controller.stopGame();

        } catch (GameException e) {

            e.printStackTrace();

            this.getMessage("stop.error").displayTo(sender);
        }
    }

    /*
        Command : /fk pvp
    */
    private void onPvPCommand(CommandSender sender, String[] args) {

        if(args.length < 2) {
            // TODO send help here.
            return;
        }

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_PVP.get())) {
            this.getMessage("pvp.no-permission").displayTo(sender);
            return;
        }

        // Only enable / disable pvp when the game is running.
        if(!this.model.isRunning()) {
            this.getMessage("pvp.game-not-running").displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("on")) {

            this.onPvPOnCommand(sender);

        } else if(args[1].equalsIgnoreCase("off")) {

            this.onPvPOffCommand(sender);

        } else ; // TODO Send usages here.
    }

    /*
        Command : /fk pvp on
    */
    private void onPvPOnCommand(CommandSender sender) {

        if(this.model.isPvPEnabled()) {

            this.getMessage("pvp.already-enabled").displayTo(sender);

        } else this.model.setPvPEnabled(true);
    }

    /*
        Command : /fk pvp off
    */
    private void onPvPOffCommand(CommandSender sender) {

        if(!this.model.isPvPEnabled()) {

            this.getMessage("pvp.already-disabled").displayTo(sender);

        } else this.model.setPvPEnabled(false);
    }

    /*
        Command : /fk assaults
    */
    private void onAssaultsCommand(CommandSender sender, String[] args) {

        if(args.length != 2) {
            // TODO send help here.
            return;
        }

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_ASSAULTS.get())) {
            this.getMessage("assaults.no-permission").displayTo(sender);
            return;
        }

        // Only enable / disable assaults when the game is running.
        if(!this.model.isRunning()) {
            this.getMessage("assaults.game-not-running").displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("on")) {

            this.onAssaultsOnCommand(sender);

        } else if(args[1].equalsIgnoreCase("off")) {

            this.onAssaultsOffCommand(sender);

        } else ; // TODO Send usages here.
    }

    /*
        Command : /fk assaults on
    */
    private void onAssaultsOnCommand(CommandSender sender) {

        if(this.model.areAssaultsEnabled()) {

            this.getMessage("assaults.already-enabled").displayTo(sender);

        } else this.model.setAssaultsEnabled(true);
    }

    /*
        Command : /fk assaults off
    */
    private void onAssaultsOffCommand(CommandSender sender) {

        if(!this.model.areAssaultsEnabled()) {

            this.getMessage("assaults.already-disabled").displayTo(sender);

        } else this.model.setAssaultsEnabled(false);
    }

    /*
        Command : /fk team
    */
    private void onTeamCommand(CommandSender sender, String[] args) {

        if(args.length < 2) {
            // TODO send help here.
            return;
        }

        // Checking if the sender has the permission to use the command.
        if(!sender.hasPermission(Permission.COMMAND_FK_TEAM.get())) {
            this.getMessage("team.no-permission").displayTo(sender);
            return;
        }

        if(args[1].equalsIgnoreCase("add")) {

            this.onTeamAddCommand(sender, args);

        } else if(args[1].equalsIgnoreCase("remove")) {

            this.onTeamRemoveCommand(sender, args);

        } else ; // TODO Send usages here.
    }

    /*
        Command : /fk team add <player> <team>
    */
    private void onTeamAddCommand(CommandSender sender, String[] args) {

        if(args.length != 4) {
            // TODO send help here.
            return;
        }

        Player target = Bukkit.getPlayerExact(args[2]);

        // Checking if the targeted player is valid.
        if(target == null) {
            this.getMessage("team.player-not-found").displayTo(sender);
            return;
        }

        Optional<? extends FKTeam> optional = this.model.getTeamByName(args[3]);

        // Checking if the targeted team is valid.
        if(!optional.isPresent()) {
            this.getMessage("team.team-not-found").displayTo(sender);
            return;
        }

        FKPlayer gamePlayer = this.model.getFKPlayer(target.getUniqueId());

        try {

            // If the target is already in a team, removing him from it.
            if(this.model.hasTeam(target.getUniqueId())) this.controller.removeTeam(gamePlayer);

            FKTeamPlayer teamPlayer = this.controller.addTeam(gamePlayer, optional.get());

            Message message = this.getMessage("team.add.player-added");
            message.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), target.getName());
            message.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), teamPlayer.getTeam().getDisplayName());

            message.displayTo(sender);

        } catch (GameException e) { e.printStackTrace(); }
    }

    /*
        Command : /fk team remove <player>
    */
    private void onTeamRemoveCommand(CommandSender sender, String[] args) {

        if(args.length != 3) {
            // TODO send help here.
            return;
        }

        Player target = Bukkit.getPlayerExact(args[2]);

        // Checking if the targeted player is valid.
        if(target == null) {
            this.getMessage("team.player-not-found").displayTo(sender);
            return;
        }

        if(!this.model.hasTeam(target.getUniqueId())) {
            this.getMessage("team.remove.not-in-team").displayTo(sender);
            return;
        }

        FKPlayer gamePlayer = this.model.getFKPlayer(target.getUniqueId());

        try {

            FKTeamPlayer teamPlayer = this.controller.removeTeam(gamePlayer);

            Message message = this.getMessage("team.remove.player-removed");
            message.addPlaceholder(PlaceholderEnum.PLAYER_NAME.get(), target.getName());
            message.addPlaceholder(FKPlaceholder.TEAM_NAME.get(), teamPlayer.getTeam().getDisplayName());

            message.displayTo(sender);

        } catch (GameException e) {

            e.printStackTrace();
        }
    }

    private Message getMessage(String key) {
        Text text = this.service.getMessage("command-fk." + key, Text.class);
        return new Message(text.getText());
    }
}
