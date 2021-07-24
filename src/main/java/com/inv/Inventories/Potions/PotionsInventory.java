package com.inv.Inventories.Potions;

import com.inv.Config;
import com.inv.Utilities.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionsInventory {

    public static short REGENERATION = 8193;
    public static short SWIFTNESS = 8194;
    public static short FIRE_RESISTANCE = 8195;
    public static short POISON = 8196;
    public static short HEALING = 8197;
    public static short NIGHT_VISION = 8198;
    public static short WEAKNESS = 8200;
    public static short STRENGTH = 8201;
    public static short SLOWNESS = 8202;
    public static short HARMING = 8204;
    public static short WATER_BREATHING = 8205;
    public static short INVISIBILITY = 8206;

    public static short[] getAllPotionIDs() {
        return new short[] {REGENERATION, SWIFTNESS, FIRE_RESISTANCE, POISON, HEALING, NIGHT_VISION, WEAKNESS, STRENGTH, SLOWNESS, HARMING, WATER_BREATHING, INVISIBILITY};
    }

    public static Inventory getInventory(ItemStack item, String name) {
        Inventory inv = Bukkit.createInventory(null, 45, Config.getPotionsTitle());
        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta mitem = black.getItemMeta();
        mitem.setDisplayName("");
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        black.setItemMeta(mitem);
        gray.setItemMeta(mitem);
        for (int i = 0; i < 45; i++) {
            if ((i >= 11 && i <= 15) || i >= 19 && i <= 25) continue;
            if (i < 9 || i >= 36 || (i + 1)%9 == 0 || i%9 == 0) {
                inv.setItem(i, black);
            } else {
                inv.setItem(i, gray);
            }
        }
        ArrayList<ItemStack> potions = getAllPotionItems(item);
        for (ItemStack potion : potions) {
            inv.addItem(potion);
        }
        ItemMeta per = inv.getItem(0).getItemMeta();
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING, "potions");
        per.getPersistentDataContainer().set(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING, name);
        inv.getItem(0).setItemMeta(per);
        inv.setItem(31, item);
        inv.setItem(40, getBackItem());
        return inv;
    }

    public static ArrayList<ItemStack> getAllPotionItems(ItemStack item) {
        ArrayList<ItemStack> items = new ArrayList<>();
        PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
        short[] ids = getAllPotionIDs();
        for (int id : ids) {
            boolean exist = false;
            int power = 1;
            int duration = 30;
            if (pc.has(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY)) {
                int[] cIDs = pc.get(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY);
                int[] cPowers = pc.get(NamespacedKey.minecraft("potion_power"), PersistentDataType.INTEGER_ARRAY);
                int[] cDurations = pc.get(NamespacedKey.minecraft("potion_duration"), PersistentDataType.INTEGER_ARRAY);
                for (int i = 0; i < cIDs.length; i++) {
                    if (id == cIDs[i]) {
                        power = cPowers[i];
                        duration = cDurations[i];
                        exist = true;
                        break;
                    }
                }
            }
            items.add(getPotionItem((short) id, power, duration, exist));
        }
        return items;
    }

    public static PotionEffectType getType(short type) {
        if (type == REGENERATION) return PotionEffectType.REGENERATION;
        else if (type == SWIFTNESS) return PotionEffectType.SPEED;
        else if (type == FIRE_RESISTANCE) return PotionEffectType.FIRE_RESISTANCE;
        else if (type == POISON) return PotionEffectType.POISON;
        else if (type == HEALING) return PotionEffectType.HEAL;
        else if (type == NIGHT_VISION) return PotionEffectType.NIGHT_VISION;
        else if (type == WEAKNESS) return PotionEffectType.WEAKNESS;
        else if (type == STRENGTH) return PotionEffectType.INCREASE_DAMAGE;
        else if (type == SLOWNESS) return PotionEffectType.SLOW;
        else if (type == HARMING) return PotionEffectType.HARM;
        else if (type == WATER_BREATHING) return PotionEffectType.WATER_BREATHING;
        else if (type == INVISIBILITY) return PotionEffectType.INVISIBILITY;
        else return null;
    }

    public static PotionType getPType(short type) {
        if (type == REGENERATION) return PotionType.REGEN;
        else if (type == SWIFTNESS) return PotionType.SPEED;
        else if (type == FIRE_RESISTANCE) return PotionType.FIRE_RESISTANCE;
        else if (type == POISON) return PotionType.POISON;
        else if (type == HEALING) return PotionType.INSTANT_HEAL;
        else if (type == NIGHT_VISION) return PotionType.NIGHT_VISION;
        else if (type == WEAKNESS) return PotionType.WEAKNESS;
        else if (type == STRENGTH) return PotionType.STRENGTH;
        else if (type == SLOWNESS) return PotionType.SLOWNESS;
        else if (type == HARMING) return PotionType.INSTANT_DAMAGE;
        else if (type == WATER_BREATHING) return PotionType.WATER_BREATHING;
        else if (type == INVISIBILITY) return PotionType.INVISIBILITY;
        else return null;
    }

    public static ItemStack getPotionItem(short type, int power, int duration, boolean added) {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta mitem = (PotionMeta) item.getItemMeta();
        PotionType ptype = getPType(type);
        mitem.setDisplayName(Config.getPotionsInvItemName(ptype.name(), power, duration));
        ArrayList<String> litem = Config.getPotionsInvItemLore(ptype.name(), power, duration);
        if (added) {
            for (String str : Config.getPotionsInvItemLoreRemove(ptype.name(), power, duration)) {
                litem.add(str);
            }
        } else {
            for (String str : Config.getPotionsInvItemLoreAdd(ptype.name(), power, duration)) {
                litem.add(str);
            }
        }
        mitem.setLore(litem);
        mitem.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        mitem.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER, (int) type);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("power"), PersistentDataType.INTEGER, power);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("duration"), PersistentDataType.INTEGER, duration);
        item.setItemMeta(mitem);
        if (added) {
            mitem.setBasePotionData(new PotionData(ptype));
        } else {
            mitem.setBasePotionData(new PotionData(PotionType.WATER));
        }
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getBackItem() {
        ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getBackName());
        mitem.setLore(Config.getBackLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static boolean checkType(Inventory inv) {
        ItemStack item = inv.getItem(0);
        if (item == null) return false;
        PersistentDataContainer pd = item.getItemMeta().getPersistentDataContainer();
        return pd.has(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING)? pd.get(NamespacedKey.minecraft("invtype"), PersistentDataType.STRING).equalsIgnoreCase("potions") : false;
    }
}
