package com.inv.Inventories.Potions;

import com.inv.Config;
import com.inv.CustomFoodPlugin;
import com.inv.Inventories.CustomItem.CustomItemEvents;
import com.inv.Inventories.CustomItem.CustomItemInventory;
import com.inv.Inventories.CustomRecipe.CustomRecipeInventory;
import com.inv.Utilities.IntArr;
import com.inv.Utilities.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionsEvents implements Listener {

    private static ArrayList<String> waitList = new ArrayList<>();

    public static void openInventory(Player p, Inventory inv) {
        if (!waitList.contains(p.getName())) {
            addWait(p.getName());
        }
        p.openInventory(inv);
    }

    public static void addWait(String pName) {
        waitList.add(pName);
        Bukkit.getScheduler().runTaskLater(CustomFoodPlugin.instance, new Runnable() {
            @Override
            public void run() {
                waitList.remove(pName);
            }
        }, 2L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv == null) return;
        if (PotionsInventory.checkType(inv)) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                int slot = e.getSlot();
                if (item.getType() == Material.POTION) {
                    ItemMeta mitem = item.getItemMeta();
                    PersistentDataContainer pd = mitem.getPersistentDataContainer();
                    if (pd.has(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER)) {
                        int id = pd.get(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER);
                        int power = pd.get(NamespacedKey.minecraft("power"), PersistentDataType.INTEGER);
                        int duration = pd.get(NamespacedKey.minecraft("duration"), PersistentDataType.INTEGER);
                        ItemStack edit = inv.getItem(31);
                        ItemMeta medit = edit.getItemMeta();
                        PersistentDataContainer pc = medit.getPersistentDataContainer();
                        ArrayList<Integer> newIDs = null, newDurations = null, newPowers = null;
                        if (pc.has(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY)) {
                            boolean found = false;
                            int[] IDs = pc.get(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY);
                            int[] durations = pc.get(NamespacedKey.minecraft("potion_duration"), PersistentDataType.INTEGER_ARRAY);
                            int[] powers = pc.get(NamespacedKey.minecraft("potion_power"), PersistentDataType.INTEGER_ARRAY);
                            newIDs = IntArr.toArrayList(IDs);
                            newDurations = IntArr.toArrayList(durations);
                            newPowers = IntArr.toArrayList(powers);
                            for (int i = 0; i < newIDs.size(); i++) {
                                if (newIDs.get(i) == id) {
                                    if (e.getClick() == ClickType.MIDDLE) {
                                        newIDs.remove(i);
                                        newDurations.remove(i);
                                        newPowers.remove(i);
                                    } else if (e.getClick() == ClickType.LEFT) {
                                        newDurations.set(i, newDurations.get(i) + 30);
                                    } else if (e.getClick() == ClickType.RIGHT) {
                                        newDurations.set(i, Math.max(30, newDurations.get(i) - 30));
                                    } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                        newPowers.set(i, Math.min(newPowers.get(i) + 1, 10));
                                    } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                        newPowers.set(i, Math.max(1, newPowers.get(i) - 1));
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (e.getClick() == ClickType.LEFT) {
                                    newIDs.add(id);
                                    newDurations.add(duration + 30);
                                    newPowers.add(power);
                                } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                    newIDs.add(id);
                                    newDurations.add(duration);
                                    newPowers.add(power + 1);
                                } else {
                                    newIDs.add(id);
                                    newDurations.add(duration);
                                    newPowers.add(power);
                                }
                            }
                        } else {
                            if (e.getClick() == ClickType.LEFT) {
                                newIDs = new ArrayList<>(Arrays.asList(id));
                                newDurations = new ArrayList<>(Arrays.asList(duration + 30));
                                newPowers = new ArrayList<>(Arrays.asList(power));
                            } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                newIDs = new ArrayList<>(Arrays.asList(id));
                                newDurations = new ArrayList<>(Arrays.asList(duration));
                                newPowers = new ArrayList<>(Arrays.asList(power + 1));
                            } else {
                                newIDs = new ArrayList<>(Arrays.asList(id));
                                newDurations = new ArrayList<>(Arrays.asList(duration));
                                newPowers = new ArrayList<>(Arrays.asList(power));
                            }
                        }
                        pc.set(NamespacedKey.minecraft("potion_id"), PersistentDataType.INTEGER_ARRAY, IntArr.toArray(newIDs));
                        pc.set(NamespacedKey.minecraft("potion_power"), PersistentDataType.INTEGER_ARRAY, IntArr.toArray(newPowers));
                        pc.set(NamespacedKey.minecraft("potion_duration"), PersistentDataType.INTEGER_ARRAY, IntArr.toArray(newDurations));
                        edit.setItemMeta(medit);
                        openInventory(p, PotionsInventory.getInventory(inv.getItem(31), inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING)));
                    }
                } else if (item.equals(PotionsInventory.getBackItem())) {
                    p.openInventory(CustomItemInventory.getInventory(inv.getItem(31), inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING)));
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (waitList.contains(e.getPlayer().getName())) {
            return;
        }
        if (PotionsInventory.checkType(inv)) {
            Bukkit.getScheduler().runTaskLater(CustomFoodPlugin.instance, new Runnable() {
                @Override
                public void run() {
                    openInventory(p, CustomItemInventory.getInventory(inv.getItem(31), inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING)));
                }
            }, 1L);
        } else if (CustomItemInventory.checkType(inv)) {
            if (waitList.contains(p.getName())) return;
            if (inv.getItem(13) == null) return;
            if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                p.getInventory().setItemInMainHand(inv.getItem(13));
            } else if (p.getInventory().getItemInOffHand() == null || p.getInventory().getItemInOffHand().getType() == Material.AIR) {
                p.getInventory().setItemInOffHand(inv.getItem(13));
            } else {
                p.getInventory().addItem(inv.getItem(13));
            }
            Config.addEditedItem(inv.getItem(13), inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("itemname"), PersistentDataType.STRING));
        } else if (CustomRecipeInventory.checkType(inv)) {
            if (waitList.contains(p.getName())) return;
            for (int i = 11; i < 32; i++) {
                if ((i + 1)%9 == 3 || (i + 1)%9 == 4 || (i + 1)%9 == 5) {
                    ItemStack iitem = inv.getItem(i);
                    if (iitem != null) {
                        p.getInventory().addItem(inv.getItem(i));
                    }
                }
            }
            ItemStack result = inv.getItem(24);
            if (result != null) {
                p.getInventory().addItem(result);
            }
        }
    }
}
