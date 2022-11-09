package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

// Class to handle the /ab pick command
// This command lists the teams' players to the entity that sent the command
public class TeamsCommandHandler extends CommandHandler
{

    public TeamsCommandHandler()
    {
        super(0);
    }

    // Method called when the /ab teams command is issued
    // The command can be called at any game status, so no need to check for it
    protected void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument long
        if (args.length != 1)
        {
            commandSender.sendMessage("Error : incorrect number of arguments. Usage: /ab teams");
        }

        // We get the list of the red players
        String redTeam = plugin.getGame()
                .getRedTeamPlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.joining(ChatColor.WHITE + ", " + ChatColor.RED));

        // We get the list of the blue players
        String blueTeam = plugin.getGame()
                .getBlueTeamPlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.joining(ChatColor.WHITE + ", " + ChatColor.BLUE));

        // Then we send the message to the commandSender
        commandSender.sendMessage("Red team: " + redTeam);
        commandSender.sendMessage("Blue team: " + blueTeam);
    }

}
