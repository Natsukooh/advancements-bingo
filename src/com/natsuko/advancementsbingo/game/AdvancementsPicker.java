package com.natsuko.advancementsbingo.game;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;

import java.util.*;

// Class to hold the list of the advancements used during a game
public class AdvancementsPicker
{

    // A list of all the existing advancements that will be built when the constructor is called
    private final List<Advancement> allAdvancements;

    // A map of the chosen advancements, with the advancement as a key and the team that completed it as a value
    // A value of null indicates that no team has completed the advancement yet
    private final Map<Advancement, Team> pickedAdvancements;

    // Constructor
    // Initializes the list of all the existing advancements
    public AdvancementsPicker()
    {
        pickedAdvancements = new HashMap<>();

        allAdvancements = new ArrayList<>();
        initializeAdvancements();
    }

    // Method to get the list of all the existing advancements
    // It uses the advancementIterator method of Bukkit to get them
    private void initializeAdvancements()
    {
        Bukkit.advancementIterator()
                .forEachRemaining(advancement ->
                {
                    // There are some advancements that we don't want
                    // They have a display of null (I honestly don't know what are these advancements, so we just skip them)
                    if (advancement.getDisplay() != null)
                    {
                        // If the advancement seems correct, we add it to the list of all the advancements
                        allAdvancements.add(advancement);
                    }
                });
    }

    // Method to randomly pick the advancements to be used in a game
    // The amount of advancements is set to 25. Might change in the future
    public void pickAdvancements()
    {
        // We prepare the list of the 25 random indexes
        List<Integer> indexes = new ArrayList<>();
        Random random = new Random();

        // While the indexes list is not 25 indexes long, we keep picking a random number and checking if we picked it already or not
        while (indexes.size() < 25)
        {
            // Pick a random number
            int index = random.nextInt(allAdvancements.size());
            // If we already picked the number previously, then we don't add it and loop again
            if (!indexes.contains(index))
            {
                // Else, add it
                indexes.add(index);
            }
        }

        // For each index i, we pick the ith advancement and add it to the list of the picked advancements
        indexes.forEach(index -> pickedAdvancements.put(allAdvancements.get(index), null));
    }

    public Map<Advancement, Team> getPickedAdvancements()
    {
        return pickedAdvancements;
    }
}
