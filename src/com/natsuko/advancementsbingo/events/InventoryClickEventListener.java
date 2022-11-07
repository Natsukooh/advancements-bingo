package com.natsuko.advancementsbingo.events;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

// Class to handle the InventoryClickEvent
// The main usage of this class is to decide what to do when a player clicks an item from an inventory menu
// For now, we just cancel the event, but in the future, clicking at items in menus may be used to control some things
public class InventoryClickEventListener implements Listener
{


    // Plugin instance to access game data
    Plugin plugin;

    // Constructor that receives the plugin instance
    public InventoryClickEventListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    // Method handling the event
    // If the inventory is our menu ,the event is cancelled
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        // We compare the inventory title to our menu inventory's
        if (event.getView()
                .getTitle()
                .equals("Advancements list"))
        {
            event.setCancelled(true);
        }
    }

}
