package com.github.syr0ws.fallenkingdom.commands;

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

    public CommandPvP(Plugin plugin) {
        this.plugin = plugin;
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

        return true;
    }
}
