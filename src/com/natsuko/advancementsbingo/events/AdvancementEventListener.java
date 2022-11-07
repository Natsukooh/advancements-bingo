package com.natsuko.advancementsbingo.events;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

// Class to handle the PlayerAdvancementDoneEvent
// When this event is triggered, we have to check if it is part of the game to give the points to the team that has completed it
public class AdvancementEventListener implements Listener
{

    // Plugin instance to access game data
    private final Plugin plugin;

    // Constructor that receives the plugin instance
    public AdvancementEventListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    // Method handling the event
    // We want to do something only if the game status is "PLAYING"
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event)
    {
        // We check the game status
        if (plugin.getGame()
                .getGameStatus()
                .equals(GameStatus.PLAYING))
        {
            // We get the player that triggered the event
            Player player = event.getPlayer();

            // And then we call the advancementComplete method from the Game instance to handle the rest
            plugin.getGame().advancementComplete(event.getAdvancement(), player);
        }
    }

}
