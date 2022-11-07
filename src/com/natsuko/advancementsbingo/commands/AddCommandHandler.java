package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Class to handle the /ab add <player> <blue/red> command
// The command is used to add a player in a team
public class AddCommandHandler
{

    // Method called when the /ab pick command is issued
    // It is possible only if the game status is "NOT_STARTED" or "ADVANCEMENTS_PICKED"
    // The targeted player, if they exist, are then added to the desired team, if they aren't already part of one team
    public static boolean onAddCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument length
        if (args.length != 3)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab add <player> <red/blue>");
            return false;
        }

        // If the game status is "NOT_STARTED" or "ADVANCEMENTS_PICKED", then we operate
        if (plugin.getGame().getGameStatus().equals(GameStatus.NOT_STARTED) || plugin.getGame().getGameStatus().equals(GameStatus.ADVANCEMENTS_PICKED))
        {
            // We get the player and their name
            String playerName = args[1];
            Player targetedPlayer = plugin.getServer().getPlayer(playerName);

            // if the player is not online, then we send an error message
            if (targetedPlayer == null)
            {
                commandSender.sendMessage("Error: player not found.");
                return true;
            }

            // We get the requested team
            String team = args[2];

            // If the team is not either red or blue, then we send an error message
            if (!team.equalsIgnoreCase("red") && !team.equalsIgnoreCase("blue"))
            {
                commandSender.sendMessage("Error: incorrect team. Possibilities are red and blue.");
                return true;
            }

            // We call the addPlayerToTeam method, and if the result is false, then that means that the player is already part of a team
            if (plugin.getGame().addPlayerToTeam(targetedPlayer, team))
            {
                commandSender.sendMessage("Player successfully added to team " + team.toLowerCase() + ".");
                return true;
            }
            // Else, the operation is a success
            else
            {
                commandSender.sendMessage("Error: the requested player is already registered in a team.");
                return true;
            }
        }
        // If the game status is not "NOT_STARTED" or "ADVANCEMENTS_PICKED", then we send an error message
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the game is not started.");
            return true;
        }
    }

}
