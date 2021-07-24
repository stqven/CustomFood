package com.inv.Inventories.CustomItem;

import com.inv.Config;
import com.inv.Inventories.Potions.PotionsEvents;
import com.inv.Utilities.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemInventory {

    public static Inventory getInventory(ItemStack item, String name) {
        Inventory inv = Bukkit.createInventory(null, 45, Config.getCustomItemTitle());
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
            } else {
                inv.setItem(i, gray);
            }
        }
        ItemMeta per = inv.getItem(0).getItemMeta();
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING, "customitem");
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING, name);
        int food = 0, saturation = 0;
        if (item != null) {
            PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
            if (pc.has(NamespacedKey.minecraft("food"), PersistentDataType.INTEGER)) {
                food = pc.get(NamespacedKey.minecraft("food"), PersistentDataType.INTEGER);
            } else {
            }
            if (pc.has(NamespacedKey.minecraft("saturation"), PersistentDataType.INTEGER)) {
                saturation = pc.get(NamespacedKey.minecraft("saturation"), PersistentDataType.INTEGER);
            } else {
            }
        }
        inv.getItem(0).setItemMeta(per);
        inv.setItem(20, getNameItem());
        inv.setItem(24, getFoodItem(food));
        inv.setItem(29, getHeadItem());
        inv.setItem(30, getLoreItem());
        inv.setItem(32, getSaturationItem(saturation));
        inv.setItem(33, getPotionsItem());
        inv.setItem(40, getSaveItem());
        if (item != null)
            inv.setItem(13, item);
        return inv;
    }

    public static ItemStack getNameItem() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSetNameItemName());
        mitem.setLore(Config.getSetNameItemLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getHeadItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSetHeadItemName());
        mitem.setLore(Config.getSetHeadItemLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getLoreItem() {
        ItemStack item = new ItemStack(Material.CHAIN);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSetLoreItemName());
        mitem.setLore(Config.getSetLoreItemLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getFoodItem(int value) {
        ItemStack item = new ItemStack(Material.APPLE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSetFoodItemName());
        mitem.setLore(Config.getSetFoodItemLore(value));
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("value"), PersistentDataType.INTEGER, value);
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getPotionsItem() {
        ItemStack item = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getAddPotionsItemName());
        mitem.setLore(Config.getAddPotionsItemLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getSaturationItem(int value) {
        ItemStack item = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSetSaturationItemName());
        mitem.setLore(Config.getSetSaturationItemLore(value));
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("value"), PersistentDataType.INTEGER, value);
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getSaveItem() {
        ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getSaveName());
        mitem.setLore(Config.getSaveLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static int getItemValue(ItemStack item) {
        PersistentDataContainer pd = item.getItemMeta().getPersistentDataContainer();
        return pd.has(NamespacedKey.minecraft("value"), PersistentDataType.INTEGER)? pd.get(NamespacedKey.minecraft("value"), PersistentDataType.INTEGER) : 0;
    }

    public static boolean checkType(Inventory inv) {
        ItemStack item = inv.getItem(0);
        if (item == null) return false;
        PersistentDataContainer pd = item.getItemMeta().getPersistentDataContainer();
        return pd.has(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING)? pd.get(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING).equalsIgnoreCase("customitem") : false;
    }
}
