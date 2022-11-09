package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

// Class representing an object to contain the logic of a certain command
public abstract class CommandHandler
{

    // A command has a permission level to be executed
    private final int RIGHT_LEVEL;

    // The constructor takes the permission level
    public CommandHandler(int RIGHT_LEVEL)
    {
        this.RIGHT_LEVEL = RIGHT_LEVEL;
    }

    // The command to redefine, that will contain the logic
    protected abstract void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin);

    // A method to check if the command sender has the permissions to execute the command
    // If so, we call the protected executeCommand
    public void handle(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        if (checkPermissions(commandSender, plugin))
        {
            executeCommand(commandSender, command, label, args, plugin);
        }
        else
        {
            commandSender.sendMessage("Error: you don't have permission to execute this command.");
        }
    }

    // Method checking if a player has the required permission to call the command
    private boolean checkPermissions(CommandSender sender, Plugin plugin)
    {
        // If the sender is the console, authorization granted
        if (sender instanceof ConsoleCommandSender)
        {
            return true;
        }
        // If it is a player, we have to check their permissions
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (plugin.getGame()
                    .getPermissionsManager()
                    .getPlayerRights(player) >= this.RIGHT_LEVEL)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        // If the sender is not the console nor a player, we don't allow the execution
        else
        {
            return false;
        }
    }

    // Method to build the required CommandHandler
    public static CommandHandler build(String command)
    {
        switch (command)
        {
            case "add":
                return new AddCommandHandler();

            case "remove":
                return new RemoveCommandHandler();

            case "teams":
                return new TeamsCommandHandler();

            case "pick":
                return new PickCommandHandler();

            case "start":
                return new StartCommandHandler();

            case "end":
                return new EndCommandHandler();

            case "list":
                return new ListCommandHandler();

            case "setrights":
                return new SetrightsCommandHandler();

            default:
                return null;
        }
    }
}
