package com.natsuko.advancementsbingo.events;

import com.natsuko.advancementsbingo.game.GameStatus;
import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// Class to handle the PlayerJoinEvent
// For now, we want a player to be unable to join the server if the game is playing. In the future, a player that is part of a team must be able to join
public class PlayerJoinEventListener implements Listener
{

    // Plugin instance to access game data
    Plugin plugin;

    // Constructor that receives the plugin instance
    public PlayerJoinEventListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    // Method handling the event
    // If the status is not "NOT_STARTED" or "ADVANCEMENTS_PICKED", then we kick the player, else we teleport them to the waiting room
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (plugin.getGame().getGameStatus().equals(GameStatus.NOT_STARTED) || plugin.getGame().getGameStatus().equals(GameStatus.ADVANCEMENTS_PICKED))
        {
            event.getPlayer().teleport(plugin.getGame().WAITING_ROOM);
        }
        else
        {
            event.getPlayer().kickPlayer("The game has already started, you can't join !");
        }
    }
}
