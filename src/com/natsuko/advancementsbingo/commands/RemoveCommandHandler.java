package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Class to handle the /ab remove <player> command
// The command is used to remove a player from the team they're part of
public class RemoveCommandHandler extends CommandHandler
{

    public RemoveCommandHandler()
    {
        super(1);
    }

    // Method called when the /ab remove command is issued
    // It must be a /ab remove <player> command, with <player> the name of an online player
    // If the player is found and part of a team, they're removed from the team
    protected void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 2 arguments long
        if (args.length != 2)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab remove <player>");
        }

        // We get the desired player
        String playerName = args[1];
        Player targetedPlayer = plugin.getServer().getPlayer(playerName);

        // If the player is null, they've not been found, so we send an error message
        if (targetedPlayer == null)
        {
            commandSender.sendMessage("Error: player not found.");
        }

        // If the player was found, we try to remove them from the team they're part of
        if (!plugin.getGame().removePlayerFromTeam(targetedPlayer))
        {
            commandSender.sendMessage("Error: the requested player is not registered in any team.");
        }
        // But if the result is false, that means the player wasn't part of any team, so we send an error message
        else
        {
            commandSender.sendMessage("Player successfully removed from their team.");
        }
    }

}
