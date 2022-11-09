package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

// Class to handle the /ab setrights <player> <right level> command
// The command is used to set the rights of a player to a certain level
public class SetrightsCommandHandler extends CommandHandler
{

    public SetrightsCommandHandler()
    {
        super(3);
    }

    // Method called when the /ab pick command is issued
    // This command can only be executed by the console !
    // Its job is to get the specified player and the rights level, check if the player exists, and set them the requested right level
    protected void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 3 argument length
        if (args.length != 3)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab setrights <player> <right level>");
        }

        // We check that it is the server console which issued the command
        if (commandSender instanceof ConsoleCommandSender)
        {
            // We get the specified player
            Player targetedPlayer = Bukkit.getPlayer(args[1]);

            // If the result is null, then the player is not online
            if (targetedPlayer == null)
            {
                commandSender.sendMessage("Error: player not found.");
            }

            int rightLevel = 0;

            // The right level parsing may throw a NumberFormatException
            // If so, we send back an error message
            try
            {
                rightLevel = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e)
            {
                commandSender.sendMessage("Error: invalid right level.");
            }

            // If we got here, the command is correct ; we can set the rights to the desired player
            plugin.getGame()
                    .getPermissionsManager()
                    .setPlayerRights(targetedPlayer, rightLevel);

            commandSender.sendMessage("Right level of " + targetedPlayer.getName() + " set to " + rightLevel + " successfully.");
        }
        // If the command sender is not the server console, we send back an error message
        else
        {
            commandSender.sendMessage("Error: this command can only be issued by the server console.");
        }
    }
}
