package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

// Class to handle the /ab pick command
// The command is used to start the game
public class StartCommandHandler
{

    // Method called when the /ab start command is issued
    // The game state must be "ADVANCEMENTS_PICKED" and nothing else
    public static boolean onStartCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument long
        if (args.length != 1)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab start");
            return false;
        }

        // We check that the game status is "ADVANCEMENTS_PICKED"
        if (plugin.getGame().getGameStatus().equals(GameStatus.ADVANCEMENTS_PICKED))
        {
            // If the command call is correct, we call the start method from the game instance
            plugin.getGame().start();
        }
        // If the game status is not "ADVANCEMENTS_PICKED", then we send an error message
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the advancements are picked. Use the /ab pick command to pick them.");
            return true;
        }

        return true;
    }

}
