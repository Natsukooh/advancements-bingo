package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

// Class to handle the /ab pick command
// The command is used to pick the 25 advancements that will be used during the game
public class PickCommandHandler
{

    // Method called when the /ab pick command is issued
    // If the game status is "NOT_STARTED", then the advancements are picked, and the game status becomes "ADVANCEMENTS_PICKED"
    // If the game status is "ADVANCEMENTS_PICKED", then the advancements are re-picked, and the status doesn't change
    // We always return true, because false would mean wrong command in the sense of Spigot, and print a command usage that we don't want
    public static boolean onPickCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument long
        if (args.length != 1)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab pick");
            return true;
        }

        // If the game status is "NOT_STARTED" or "ADVANCEMENTS_PICKED", then we operate
        if (plugin.getGame().getGameStatus().equals(GameStatus.NOT_STARTED) || plugin.getGame().getGameStatus().equals(GameStatus.ADVANCEMENTS_PICKED))
        {
            // The advancements are picked (logic for that is not here)
            plugin.getGame().pickAdvancements();

            // Chat messages
            Bukkit.broadcastMessage("advancements picked !");
            Bukkit.broadcastMessage("The advancements are: " +
                    plugin.getGame()
                            .getAdvancementsPicker()
                            .getPickedAdvancements()
                            .keySet()
                            .stream()
                            .map(advancement -> advancement.getDisplay().getTitle())
                            .collect(Collectors.joining(", ")));
            Bukkit.broadcastMessage("You can now start the game with /ab start, or re-pick the advancements with /ab pick again.");
            Bukkit.broadcastMessage("To see the advancements list, use the /ab list command");

            return true;
        }
        // If the game status is not "NOT_STARTED" or "ADVANCEMENTS_PICKED", then we send an error message
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the game is not started.");
            return true;
        }
    }

}
