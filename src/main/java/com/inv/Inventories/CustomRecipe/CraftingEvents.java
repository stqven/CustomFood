package com.inv.Inventories.CustomRecipe;

import com.inv.BetterRecipes.BetterRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingEvents implements Listener {

    @EventHandler
    public void onCraftPrepare(PrepareItemCraftEvent e) {
        ItemStack[] invMatrix = e.getInventory().getMatrix(), matrix = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            if (invMatrix[i] != null) {
                matrix[i] = invMatrix[i];
            } else {
                matrix[i] = new ItemStack(Material.AIR);
            }
        }
        for (BetterRecipe recipe : BetterRecipe.getRecipes()) {
            if (recipe.compareToCrafting(matrix)) {
                e.getInventory().setItem(0, recipe.getResult());
            }
        }
    }

}
