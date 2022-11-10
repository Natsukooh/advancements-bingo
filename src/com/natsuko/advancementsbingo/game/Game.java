package com.natsuko.advancementsbingo.game;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

// This class represents a game of advancements bingo
public class Game
{

    // The game status
    private GameStatus gameStatus;

    // The list of players in the blue team
    private final List<Player> blueTeamPlayers;

    // The list of players in the red team
    private final List<Player> redTeamPlayers;

    // The advancements picker objects
    private final AdvancementsPicker advancementsPicker;

    // The permissions manager object
    private final PermissionsManager permissionsManager;

    // The scoreboard to display live score during a game
    private Scoreboard scoreboard;

    // The location of the waiting room
    public final Location WAITING_ROOM = new Location(Bukkit.getWorld("world"), 0, 201, 0);

    // The location of the spawn (the highest block at 0,0)
    public final Location SPAWN = new Location(Bukkit.getWorld("world"), 0, getLowestAirBlock(0, 0), 0);

    public Game()
    {
        // At the beginning the status is "NOT_STARTED"
        this.gameStatus = GameStatus.NOT_STARTED;

        // Teams are instanced
        this.blueTeamPlayers = new ArrayList<>();
        this.redTeamPlayers = new ArrayList<>();

        // Advancements picker is instanced
        this.advancementsPicker = new AdvancementsPicker();

        // Permissions manager is instanced
        this.permissionsManager = new PermissionsManager();

        // We create the spawn room
        this.createSpawnRoom();
        // We teleport all online players (if any) in the spawn room
        this.teleportPlayersInSpawnRoom();

        // We initialize everything related to the game's scoreboard
        this.initializeScoreboard();
    }

    private void initializeScoreboard()
    {
        // We create a new scoreboard
        this.scoreboard = Bukkit.getScoreboardManager()
                .getNewScoreboard();

        // We create the objective to display
        scoreboard.registerNewObjective("Score", Criteria.DUMMY, "Score", RenderType.INTEGER)
                .setDisplaySlot(DisplaySlot.SIDEBAR);

        // We add both teams to the objective
        scoreboard.registerNewTeam("Red")
                .setColor(ChatColor.RED);
        scoreboard.registerNewTeam("Blue")
                .setColor(ChatColor.BLUE);

        // We display both teams' scores
        scoreboard.getObjective("Score")
                .getScore(ChatColor.BLUE + "Blue        ")
                .setScore(0);
        scoreboard.getObjective("Score")
                .getScore(ChatColor.RED + "Red        ")
                .setScore(0);

        // We display the objective to all online players
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.setScoreboard(scoreboard));
    }

    // Method to go from the "NOT_STARTED" to the "ADVANCEMENTS_PICKED" status
    // Calls the pickAdvancements method of the advancement picker and changes the status
    // Also displays a title to all the players and send them a message
    public void pickAdvancements()
    {
        this.advancementsPicker.pickAdvancements();

        redTeamPlayers.forEach(player -> {
            player.sendTitle("Advancements picked", "You can see them by issuing the /ab list command !", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 0.5F);
            player.sendMessage("Advancements picked! You can see them by issuing the /ab list command !");
        });
        blueTeamPlayers.forEach(player -> {
            player.sendTitle("Advancements picked", "You can see them by issuing the /ab list command !", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 0.5F);
            player.sendMessage("Advancements picked! You can see them by issuing the /ab list command !");
        });

        this.gameStatus = GameStatus.ADVANCEMENTS_PICKED;
    }

    // Method to go from the "ADVANCEMENTS_PICKED" to the "PLAYING" status
    // We teleport all players to the spawn and change the status
    // We also display a title and play a sound to all the players
    public void start()
    {
        blueTeamPlayers.forEach(player -> player.teleport(SPAWN));
        redTeamPlayers.forEach(player -> player.teleport(SPAWN));

        redTeamPlayers.forEach(player -> {
            player.sendTitle("Game starts", "May the odds be ever in your favor !", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.AMBIENT_CRIMSON_FOREST_MOOD, 0.6F, 0.5F);
        });
        blueTeamPlayers.forEach(player -> {
            player.sendTitle("Game starts", "May the odds be ever in your favor !", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.AMBIENT_CRIMSON_FOREST_MOOD, 0.6F, 0.5F);
        });

        this.gameStatus = GameStatus.PLAYING;
    }

    // Method to check if the game is over or not
    // It is over if the total amount of advancements done is 25
    // If it is over, the status goes from "PLAYING" to "ENDING"
    public void checkGameOver()
    {
        // We get the scores
        int redTeamScore = this.getTeamScore(Team.RED);
        int blueTeamScore = this.getTeamScore(Team.BLUE);

        // A total of 25 means all advancements are done
        if (redTeamScore + blueTeamScore == 25)
        {
            // The actions to do when ending the game are done in another method
            this.forceGameEnd();
        }
    }

    // Method to effectively end a game
    public void forceGameEnd()
    {
        // We get the scores
        int redTeamScore = this.getTeamScore(Team.RED);
        int blueTeamScore = this.getTeamScore(Team.BLUE);

        // We teleport all players back to the waiting room
        blueTeamPlayers.forEach(player -> player.teleport(WAITING_ROOM));
        redTeamPlayers.forEach(player -> player.teleport(WAITING_ROOM));

        // We announce the winner
        // In the chat, but also by displaying a title to all the players
        // We also send fireworks of the color of the winner
        if (redTeamScore > blueTeamScore)
        {
            Bukkit.broadcastMessage("Team " + ChatColor.RED + "Red" + ChatColor.WHITE + " wins the match !");
            redTeamPlayers.forEach(player -> {
                player.sendTitle("Team " + ChatColor.RED + "Red" + ChatColor.WHITE + " wins the match !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            blueTeamPlayers.forEach(player -> {
                player.sendTitle("Team " + ChatColor.RED + "Red" + ChatColor.WHITE + " wins the match !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            endOfGameFireworks(Team.RED);
        }
        else if (redTeamScore < blueTeamScore)
        {
            Bukkit.broadcastMessage("Team " + ChatColor.BLUE + "Blue" + ChatColor.WHITE + " wins the match !");
            redTeamPlayers.forEach(player -> {
                player.sendTitle("Team " + ChatColor.BLUE + "Blue" + ChatColor.WHITE + " wins the match !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            blueTeamPlayers.forEach(player -> {
                player.sendTitle("Team " + ChatColor.BLUE + "Blue" + ChatColor.WHITE + " wins the match !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            endOfGameFireworks(Team.BLUE);
        }
        else
        {
            Bukkit.broadcastMessage("The game ends in a draw !");
            redTeamPlayers.forEach(player -> {
                player.sendTitle("The game ends in a draw !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            blueTeamPlayers.forEach(player -> {
                player.sendTitle("The game ends in a draw !", "Congratulations, everyone !", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 0.5F);
            });
            endOfGameFireworks(null);
        }

        // We change the status
        this.gameStatus = GameStatus.ENDING;
    }

    // Method to summon a firework of the desired color
    // To use when a game ends to add visual effects
    private void endOfGameFireworks(Team team)
    {
        Location location = new Location(Bukkit.getWorld("world"), 0, 203, 0);
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(2);
        meta.addEffect(FireworkEffect.builder().withColor(team == null ? Color.WHITE : (team.equals(Team.RED) ? Color.RED : Color.BLUE)).flicker(true).build());

        firework.setFireworkMeta(meta);
    }

    // Method to add a player to a team
    // If the player is already part of a team, we return false
    // Else we add them to the team and return true
    public boolean addPlayerToTeam(Player player, String team)
    {
        // If the player is not already part of a team, we add them to the desired team
        if (!isPlayerPlaying(player))
        {
            if (team.equalsIgnoreCase("blue"))
            {
                blueTeamPlayers.add(player);
                this.scoreboard
                        .getTeam("Blue")
                        .addEntry(player.getName());
                return true;
            }

            if (team.equalsIgnoreCase("red"))
            {
                redTeamPlayers.add(player);
                this.scoreboard
                        .getTeam("Red")
                        .addEntry(player.getName());
                return true;
            }
        }

        // If the player is already part of a team, or the team is neither "red" or "blue", we return false
        return false;
    }

    // Method to remove a player from a team
    // Returns true if the player was part of a team, and false otherwise
    // We must remove the player from the team (the blueTeamPlayers or redTeamPlayers list), and also from the scoreboard team
    public boolean removePlayerFromTeam(Player player)
    {
        switch (getPlayerTeam(player))
        {
            case BLUE:
                this.scoreboard
                        .getTeam("Blue")
                        .removeEntry(player.getName());
                this.blueTeamPlayers
                        .remove(player);

            case RED:
                this.scoreboard
                        .getTeam("Red")
                        .removeEntry(player.getName());
                this.redTeamPlayers
                        .remove(player);

            default:
                return false;
        }
    }

    // Method to check if a player is part of a team
    // Returns true if the player is in one of the teams, and false otherwise
    public boolean isPlayerPlaying(Player player)
    {
        return redTeamPlayers.contains(player) || blueTeamPlayers.contains(player);
    }

    // Method to get the team of a player
    // Returns the team or null if the player is not in a team
    public Team getPlayerTeam(Player player)
    {
        if (redTeamPlayers.contains(player))
        {
            return Team.RED;
        }

        if (blueTeamPlayers.contains(player))
        {
            return Team.BLUE;
        }

        return null;
    }

    // Method to get the amount of advancements completed by a team
    public int getTeamScore(Team team)
    {
        return (int) this.getAdvancementsPicker()
                .getPickedAdvancements()
                .keySet()
                .stream()
                .filter(advancement -> {
                    // We keep only the advancements completed by the requested team, to then count them
                    Team status = this.advancementsPicker.getPickedAdvancements().get(advancement);
                    return status != null && status.equals(team);
                })
                .count();
    }

    // Method to notify the game that an advancement has been completed
    // We need to check if the advancement is part of the 25 chosen ones, and if the player is playing the game
    public void advancementComplete(Advancement advancement, Player player)
    {
        // We have to make sure that the advancement is part of the game
        if (this.getAdvancementsPicker()
                .getPickedAdvancements()
                .containsKey(advancement))
        {
            // Check if the player is playing the game
            if (isPlayerPlaying(player))
            {
                // We get the player's team and the status of the advancement (not completed or completed)
                Team team = getPlayerTeam(player);
                Team status = getAdvancementsPicker()
                        .getPickedAdvancements()
                        .get(advancement);

                // If the advancement has not been completed yet
                if (status == null)
                {
                    // Useful later
                    Score score;

                    // We tell the game the advancement has been done by the player's team
                    // We also have to update the game's scoreboard
                    // Players from the same team hear a level up sound
                    // Players from the other team hear a wolf grown sound
                    switch (team)
                    {
                        case RED:
                            getAdvancementsPicker()
                                    .getPickedAdvancements()
                                    .replace(advancement, Team.RED);

                            // The team's score is incremented
                            score = scoreboard.getObjective("Score")
                                    .getScore(ChatColor.RED + "Red        ");
                            score.setScore(score.getScore() + 1);

                            redTeamPlayers.forEach(player1 -> player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2f, 0.5F));
                            blueTeamPlayers.forEach(player1 -> player1.playSound(player1.getLocation(), Sound.ENTITY_WOLF_GROWL, 0.2f, 0.5F));

                            break;

                        case BLUE:
                            getAdvancementsPicker()
                                    .getPickedAdvancements()
                                    .replace(advancement, Team.BLUE);

                            // The team's score is incremented
                            score = scoreboard.getObjective("Score")
                                    .getScore(ChatColor.BLUE + "Blue        ");
                            score.setScore(score.getScore() + 1);

                            blueTeamPlayers.forEach(player1 -> player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2f, 0.5F));
                            redTeamPlayers.forEach(player1 -> player1.playSound(player1.getLocation(), Sound.ENTITY_WOLF_GROWL, 0.2f, 0.5F));

                            break;

                        default:
                            break;
                    }

                    // We broadcast a message to the server
                    Bukkit.broadcastMessage("Advancement " + advancement.getDisplay().getTitle() + " has been completed by team " + team.toString());
                    Bukkit.broadcastMessage("Score: "
                            + ChatColor.BLUE + getTeamScore(Team.BLUE)
                            + ChatColor.WHITE + " to "
                            + ChatColor.RED + getTeamScore(Team.RED));

                    checkGameOver();
                }
            }
        }
    }

    // Method to build the spawn room
    // 11*11 platform made of barrier (invisible block) with 2 height barrier walls
    private void createSpawnRoom()
    {
        for (int x = -5; x <= 5; x++)
        {
            for (int z = -5; z <= 5; z++)
            {
                Bukkit.getWorld("world").getBlockAt(x, 200, z).setType(Material.BARRIER);
            }
        }

        for (int x = -5; x <= 5; x++)
        {
            for (int y = 201; y <= 202; y++)
            {
                Bukkit.getWorld("world").getBlockAt(x, y, -6).setType(Material.BARRIER);
                Bukkit.getWorld("world").getBlockAt(x, y, 6).setType(Material.BARRIER);
            }
        }

        for (int z = -5; z <= 5; z++)
        {
            for (int y = 201; y <= 202; y++)
            {
                Bukkit.getWorld("world").getBlockAt(-6, y, z).setType(Material.BARRIER);
                Bukkit.getWorld("world").getBlockAt(6, y, z).setType(Material.BARRIER);
            }
        }
    }

    // Method to teleports all players in the waiting room
    private void teleportPlayersInSpawnRoom()
    {
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.teleport(WAITING_ROOM));
    }

    // Method to determinate where is the lowest air block at desired x, z
    // Used to get the spawn height
    private int getLowestAirBlock(int x, int z)
    {
        Location location;
        int y = 200;

        do
        {
            y--;
            location = new Location(Bukkit.getWorld("world"), x, y, z);
        }
        while (location.getBlock().getType().equals(Material.AIR));

        return y + 1;
    }

    public GameStatus getGameStatus()
    {
        return gameStatus;
    }

    public List<Player> getBlueTeamPlayers()
    {
        return blueTeamPlayers;
    }

    public List<Player> getRedTeamPlayers()
    {
        return redTeamPlayers;
    }

    public AdvancementsPicker getAdvancementsPicker()
    {
        return advancementsPicker;
    }

    public PermissionsManager getPermissionsManager()
    {
        return permissionsManager;
    }
}
