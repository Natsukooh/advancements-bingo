package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import com.natsuko.advancementsbingo.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Class to handle the /ab list command
// The command is used to list all the 25 advancements picked for the game, as an inventory with each advancement as an item
public class ListCommandHandler extends CommandHandler
{

    public ListCommandHandler()
    {
        super(0);
    }

    // Method called when the /ab list command is issued
    // We want the status to be something else than "NOT_STARTED"
    protected void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument length
        if (args.length != 1)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab list");
        }

        // We check that the status is not "NOT_STARTED"
        if (!plugin.getGame().getGameStatus().equals(GameStatus.NOT_STARTED))
        {
            // If the entity that issued the command is not a player, we send a message to them instead of displaying an inventory
            if (!(commandSender instanceof Player))
            {
                commandSender.sendMessage("The advancements are: " +
                        plugin.getGame()
                                .getAdvancementsPicker()
                                .getPickedAdvancements()
                                .keySet()
                                .stream()
                                .map(advancement -> {
                                    ChatColor color = ChatColor.WHITE;
                                    Team status = plugin.getGame()
                                            .getAdvancementsPicker()
                                            .getPickedAdvancements()
                                            .get(advancement);
                                    if (status != null)
                                    {
                                        switch (status)
                                        {
                                            case RED -> color = ChatColor.RED;
                                            case BLUE -> color = ChatColor.BLUE;
                                            default ->
                                            {
                                            }
                                        }
                                    }
                                    return color + advancement.getDisplay().getTitle();
                                })
                                .collect(Collectors.joining(", ")));

                commandSender.sendMessage("Score: "
                        + ChatColor.BLUE + plugin.getGame().getTeamScore(Team.BLUE)
                        + ChatColor.WHITE + " to "
                        + ChatColor.RED + plugin.getGame().getTeamScore(Team.RED));
            }
            // If the commandSender is a player, then we display the inventory
            else
            {
                plugin.getGame()
                        .showAdvancementsList((Player) commandSender);
            }
        }
        // If the status is "NOT_STARTED", we send an error message
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the advancements are picked.");
        }
    }
}
