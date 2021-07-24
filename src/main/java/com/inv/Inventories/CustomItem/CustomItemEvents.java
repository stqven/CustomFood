package com.inv.Inventories.CustomItem;

import com.inv.Config;
import com.inv.Inventories.Potions.PotionsEvents;
import com.inv.Inventories.Potions.PotionsInventory;
import com.inv.SignInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (CustomItemInventory.checkType(inv)) {
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            int slot = e.getSlot();
            e.setCancelled(true);
            if (item.equals(CustomItemInventory.getNameItem())) {
                SignInput input = new SignInput(p, inv, "Enter Item's name:");
            } else if (item.equals(CustomItemInventory.getHeadItem())) {
                if (inv.getItem(13).getType() != Material.PLAYER_HEAD) {
                    Config.sendMessage(p, Config.getItemNotHeadMessage());
                    p.closeInventory();
                    return;
                }
                SignInput input = new SignInput(p, inv, "Enter a head name:");
            } else if (item.equals(CustomItemInventory.getLoreItem())) {
                SignInput input = new SignInput(p, inv, "Enter item's lore:");
            } else if (item.equals(CustomItemInventory.getPotionsItem())) {
                PotionsEvents.addWait(p.getName());
                PotionsEvents.openInventory(p, PotionsInventory.getInventory(inv.getItem(13), inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING)));
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Config.getSetFoodItemName())) {
                int value = CustomItemInventory.getItemValue(item);
                if (e.getClick() == ClickType.LEFT) {
                    value = Math.min(value + 1, 20);
                } else if (e.getClick() == ClickType.RIGHT)
                    value = Math.min(value - 1, 20);
                else if (e.getClick() == ClickType.MIDDLE)
                    value = 0;
                inv.setItem(slot, CustomItemInventory.getFoodItem(value));
                ItemMeta m13 = inv.getItem(13).getItemMeta();
                PersistentDataContainer pc13 = m13.getPersistentDataContainer();
                pc13.set(NamespacedKey.minecraft("food"), PersistentDataType.INTEGER, value);
                inv.getItem(13).setItemMeta(m13);
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Config.getSetSaturationItemName())) {
                int value = CustomItemInventory.getItemValue(item);
                if (e.getClick() == ClickType.LEFT) {
                    value = Math.min(value + 1, 20);
                } else if (e.getClick() == ClickType.RIGHT)
                    value = Math.min(value - 1, 20);
                else if (e.getClick() == ClickType.MIDDLE)
                    value = 0;
                inv.setItem(slot, CustomItemInventory.getSaturationItem(value));
                ItemMeta m13 = inv.getItem(13).getItemMeta();
                PersistentDataContainer pc13 = m13.getPersistentDataContainer();
                pc13.set(NamespacedKey.minecraft("saturation"), PersistentDataType.INTEGER, value);
                inv.getItem(13).setItemMeta(m13);
            } else if (item.equals(CustomItemInventory.getSaveItem())) {
                p.closeInventory();
            }
        }
    }
}
