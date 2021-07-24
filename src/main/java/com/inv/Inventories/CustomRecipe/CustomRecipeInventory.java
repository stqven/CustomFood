package com.inv.Inventories.CustomRecipe;

import com.inv.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomRecipeInventory {

    public static Inventory getInventory(String name) {
        Inventory inv = Bukkit.createInventory(null, 45, Config.getCustomRecipeTitle());
        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mblack = black.getItemMeta();
        mblack.setDisplayName("");
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        black.setItemMeta(mblack);
        gray.setItemMeta(mblack);
        for (int i = 0; i < 45; i++) {
            if (i == 13) continue;
            if (i < 9 || i >= 36 || (i + 1)%9 == 0 || i%9 == 0) {
                inv.setItem(i, black);
            } else if ((i + 1)%9 != 3 && (i + 1)%9 != 4 && (i + 1)%9 != 5 && i != 24) {
                inv.setItem(i, gray);
            }
        }
        inv.setItem(40, getSaveItem());
        ItemMeta per = inv.getItem(0).getItemMeta();
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING, "customrecipe");
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING, name);
        inv.getItem(0).setItemMeta(per);
        return inv;
    }

    public static ItemStack getSaveItem() {
        ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getRSaveName());
        mitem.setLore(Config.getRSaveLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static boolean checkType(Inventory inv) {
        ItemStack item = inv.getItem(0);
        if (item == null) return false;
        PersistentDataContainer pd = item.getItemMeta().getPersistentDataContainer();
        return pd.has(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING)? pd.get(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING).equalsIgnoreCase("customrecipe") : false;
    }
}
