package com.natsuko.advancementsbingo;

import com.natsuko.advancementsbingo.commands.AdvancementsBingoCommandHandler;
import com.natsuko.advancementsbingo.events.AdvancementEventListener;
import com.natsuko.advancementsbingo.events.InventoryClickEventListener;
import com.natsuko.advancementsbingo.events.PlayerInteractEventListener;
import com.natsuko.advancementsbingo.events.PlayerJoinEventListener;
import com.natsuko.advancementsbingo.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin
{

    // The game object
    // Will be instanced in the constructor
    private Game game;

    // Method to call when the plugin is loaded
    @Override
    public void onEnable()
    {
        // We create an instance of the Game class
        game = new Game();

        // We register the events that we are listening to
        this.getServer().getPluginManager().registerEvents(new AdvancementEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);

        // We register the commands that can be used by the players
        this.getCommand("ab").setExecutor(new AdvancementsBingoCommandHandler(this));

        Bukkit.getLogger().info("Advancements bingo plugin started !!");
    }

    // Method to call when the plugin is unloaded (the server is stopped or reloaded)
    @Override
    public void onDisable()
    {
        Bukkit.getLogger().info("Advancements bingo plugin stopped !");
    }

    public Game getGame()
    {
        return game;
    }
}
