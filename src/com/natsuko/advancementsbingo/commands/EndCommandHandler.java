package com.natsuko.advancementsbingo.commands;

import com.natsuko.advancementsbingo.Plugin;
import com.natsuko.advancementsbingo.game.GameStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EndCommandHandler extends CommandHandler
{

    public EndCommandHandler()
    {
        super(2);
    }

    protected void executeCommand(CommandSender commandSender, Command command, String label, String[] args, Plugin plugin)
    {
        // The command must be 1 argument length
        if (args.length != 1)
        {
            commandSender.sendMessage("Error: incorrect number of arguments. Usage: /ab list");
        }

        if (plugin.getGame()
                .getGameStatus()
                .equals(GameStatus.PLAYING))
        {
            plugin.getGame()
                    .forceGameEnd();
        }
        else
        {
            commandSender.sendMessage("Error: that command is only possible if the advancements are picked.");
        }

    }
}
