package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

// Class to handle any /ab command
public class AdvancementsBingoCommandHandler implements CommandExecutor
{

    // Plugin instance
    Plugin plugin;

    // Constructor, which takes an instance to the plugin to access important information about the game
    public AdvancementsBingoCommandHandler(Plugin plugin)
    {
        this.plugin = plugin;
    }

    // Method that will be called when any /ab command is issued
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        // If there's no argument at all, we print the usage
        if (args.length == 0)
        {
            return false;
        }

        // Depending on which subcommand is issued, we call the right method from the right class
        switch (args[0])
        {
            case "add":
                return AddCommandHandler.onAddCommand(commandSender, command, label, args, plugin);

            case "remove":
                return RemoveCommandHandler.onRemoveCommand(commandSender, command, label, args, plugin);

            case "teams":
                return TeamsCommandHandler.onTeamsCommand(commandSender, command, label, args, plugin);

            case "pick":
                return PickCommandHandler.onPickCommand(commandSender, command, label, args, plugin);

            case "start":
                return StartCommandHandler.onStartCommand(commandSender, command, label, args, plugin);

            case "end":
                return EndCommandHandler.onEndCommand(commandSender, command, label, args, plugin);

            case "list":
                return ListCommandHandler.onListCommand(commandSender, command, label, args, plugin);

            default:
                break;
        }

        return true;
    }
}
