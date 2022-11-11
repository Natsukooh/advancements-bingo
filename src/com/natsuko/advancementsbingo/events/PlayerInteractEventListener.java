package com.natsuko.advancementsbingo.events;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

// Class to handle the InventoryClickEvent
// For now, the only purpose this class serves is to check if the player right-clicks on the item called "Advancements list"
// If so, we display the advancements list menu to the player
public class PlayerInteractEventListener implements Listener
{

    // Plugin instance to access game data
    Plugin plugin;

    // Constructor that receives the plugin instance
    public PlayerInteractEventListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    // Method called when a player right-clicks at an event
    // We must do something only if the item is the special item that allows to open the menu
    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event)
    {
        // We check if the item is the special item by its name
        if (event.getItem() != null && event.getItem().getItemMeta().getDisplayName().equals("Advancements list"))
        {
            // We have to open the menu only if it is a right-click
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                // We open the menu
                plugin.getGame().showAdvancementsList(event.getPlayer());
            }
        }
    }
}
