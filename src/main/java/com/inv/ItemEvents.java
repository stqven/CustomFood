package com.inv;

import com.inv.Inventories.Potions.PotionsEvents;
import com.inv.Inventories.Potions.PotionsInventory;
import com.inv.Utilities.IntArr;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;

public class ItemEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        int food = p.getFoodLevel();
        if (food == 20) {
            p.setFoodLevel(19);
            Bukkit.getScheduler().runTaskLater(CustomFoodPlugin.instance, new Runnable() {
                @Override
                public void run() {
                    p.setFoodLevel(20);
                }
            }, 1L);
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || item.getType().isEdible() || item.getType() == Material.POTION) return;
        applyEffects(p, true);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        applyEffects(e.getPlayer(), false);
    }

    public static void applyEffects(Player p, boolean remove) {
        int food = 0;
        int saturation = 0;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;
        PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
        if (pc.has(NamespacedKey.minecraft("food"), PersistentDataType.INTEGER)) {
            food = pc.get(NamespacedKey.minecraft("food"), PersistentDataType.INTEGER);
        } else {
        }
        if (pc.has(NamespacedKey.minecraft("saturation"), PersistentDataType.INTEGER)) {
            saturation = pc.get(NamespacedKey.minecraft("saturation"), PersistentDataType.INTEGER);
        } else {
        }
        p.setFoodLevel(p.getFoodLevel() + food);
        p.setSaturation(p.getSaturation() + saturation);

        int[] IDs = pc.get(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY);
        int[] durations = pc.get(NamespacedKey.minecraft("potion_duration"), PersistentDataType.INTEGER_ARRAY);
        int[] powers = pc.get(NamespacedKey.minecraft("potion_power"), PersistentDataType.INTEGER_ARRAY);

        for (int i = 0; i < IDs.length; i++) {
            PotionEffectType ptype = PotionsInventory.getType((short) IDs[i]);
            int power = powers[i];
            int duration = durations[i];
            p.addPotionEffect(new PotionEffect(ptype, duration*20, power - 1));
        }
        if (remove) {
            Bukkit.getScheduler().runTaskLater(CustomFoodPlugin.instance, new Runnable() {
                @Override
                public void run() {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                        p.getInventory().setItemInMainHand(item);
                    } else {
                        p.getInventory().setItemInMainHand(null);
                    }
                }
            }, 1L);
        }
    }
}
