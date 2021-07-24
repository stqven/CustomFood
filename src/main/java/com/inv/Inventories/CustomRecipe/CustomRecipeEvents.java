package com.inv.Inventories.CustomRecipe;

import com.inv.BetterRecipes.BetterRecipe;
import com.inv.Config;
import com.inv.Inventories.Potions.PotionsEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class CustomRecipeEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (CustomRecipeInventory.checkType(inv)) {
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            int slot = e.getSlot();
            if (((slot + 1)%9 != 3 && (slot + 1)%9 != 4 && (slot + 1)%9 != 5 && slot != 24) || (slot < 11 || slot > 31)) e.setCancelled(true);
            if (item == null) return;
            if (item.equals(CustomRecipeInventory.getSaveItem())) {
                ItemStack[] matrix = new ItemStack[9];
                int counter = 0;
                for (int i = 11; i < 32; i++) {
                    if ((i + 1)%9 == 3 || (i + 1)%9 == 4 || (i + 1)%9 == 5) {
                        ItemStack iitem = inv.getItem(i);
                        if (iitem != null) {
                            matrix[counter++] = inv.getItem(i);
                        } else {
                            matrix[counter++] = new ItemStack(Material.AIR);
                        }
                    }
                }
                ArrayList<BetterRecipe> recipes = BetterRecipe.getRecipes();
                for (BetterRecipe rc : recipes) {
                    if (rc.compareToCrafting(matrix)) {
                        Config.sendMessage(p, Config.getSameIngredientsMessage());
                        p.closeInventory();
                        return;
                    }
                }
                ItemStack result = inv.getItem(24);
                if (result == null || result.getType() == Material.AIR) {
                    Config.sendMessage(p, Config.getNoRecipeMessage());
                    p.closeInventory();
                    return;
                }
                BetterRecipe rr = new BetterRecipe(matrix, result, inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING));
                PotionsEvents.addWait(p.getName());
                p.closeInventory();
            }
        }
    }
}
