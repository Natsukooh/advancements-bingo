package com.natsuko.advancementsbingo.game;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

// Class to manage players' permissions regarding executing commands
public class PermissionsManager
{

    // The map defining the rights of a player
    // Each player is a key, and the associated value is the right level of the player
    // Each command has a right level, and a player is allowed to execute the command only if their right level is higher or equal to the commands' right level
    private final Map<Player, Integer> playerRights;

    // The constructor, taking the plugin instance and initializing the class variables
    public PermissionsManager()
    {
        this.playerRights = new HashMap<>();
    }

    // Sets the rights of a specific player
    public void setPlayerRights(Player player, int rights)
    {
        playerRights.put(player, rights);
    }

    // get the rights of a specific player
    // if the player is not in the map, his rights are 0
    public int getPlayerRights(Player player)
    {
        return playerRights.getOrDefault(player, 0);
    }
}
