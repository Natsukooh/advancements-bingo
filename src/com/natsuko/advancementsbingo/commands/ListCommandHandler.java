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
public class ListCommandHandler
{

    // Method called when the /ab list command is issued
    // We want the status to be something else than "NOT_STARTED"
    public static boolean onListCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument length
        if (args.length != 1)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab list");
            return false;
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
                // Casting the commandSender into a Player
                Player player = (Player) commandSender;

                // Getting the list of the 25 advancements
                List<Advancement> advancements = plugin.getGame()
                        .getAdvancementsPicker()
                        .getPickedAdvancements()
                        .keySet()
                        .stream()
                        .toList();

                // Instantiating a new Inventory that we will then fill
                Inventory advancementsList = Bukkit.createInventory(null, 45, "Advancements list");

                // We want the 25 advancements to be displayed in a 5*5 square
                // The IDs of the slots are between 0 and 8 on each line, plus 9*row
                // So on each row, we want the items on the slots 2 to 6
                // The count variable is used to iterate through the advancements list
                int count = 0;
                for(int i = 2; i < 45; i+=9)
                {
                    for(int j = 0; j < 5; j++)
                    {
                        // We call the getAdvancementItemDisplay method to get an item corresponding to the advancement to display
                        advancementsList.setItem(i+j, getAdvancementItemDisplay(advancements.get(count), plugin.getGame().getAdvancementsPicker().getPickedAdvancements()));

                        // Don't forget to increase the count to iterate over all the advancements
                        count++;
                    }
                }

                // Finally, we display the inventory to the player
                player.openInventory(advancementsList);
            }
        }
        // If the status is "NOT_STARTED", we send an error message
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the advancements are picked.");
            return true;
        }

        return true;
    }

    // Method to create an item representing an advancement
    // Each advancement has an item to represent it ; we take this item, set its name to the advancement name, and its lore to the description of the advancement
    private static ItemStack getAdvancementItemDisplay(Advancement advancement, Map<Advancement, Team> advancements)
    {
        // First, we create the ItemStack
        ItemStack itemStack = new ItemStack(advancement.getDisplay().getIcon());

        // Then we get its meta, and set its title and lore
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(advancement.getDisplay().getTitle());
        List<String> itemLore = new ArrayList<>(Collections.singleton(advancement.getDisplay().getDescription()));

        // If the advancement has already been done by a team, then we replace the item by a colored wool (red or blue) and we add a row to the lore to tell that it is completed
        Team team = advancements.get(advancement);
        // If team is not null, then the advancement has already been done
        if (team != null)
        {
            // Depending on which team has done it, we set a blue or red wool and add the right sentence to the lore
            if (team.equals(Team.BLUE))
            {
                itemStack.setType(Material.BLUE_WOOL);
                itemLore.add("BLUE team has completed this advancement !");
            }

            if (team.equals(Team.RED))
            {
                itemStack.setType(Material.RED_WOOL);
                itemLore.add("RED team has completed this advancement !");
            }
        }

        // Finally we set the title and lore to the item
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        // We can now return it
        return itemStack;
    }
}
