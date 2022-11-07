package com.natsuko.advancementsbingo.events;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener implements Listener
{

    Plugin plugin;

    public InventoryClickEventListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getView()
                .getTitle()
                .equals("Advancements list"))
        {
            event.setCancelled(true);
        }
    }

}
