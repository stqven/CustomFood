package com.inv;

import com.inv.Utilities.Pair;
import com.inv.Utilities.TimeConverter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    public static void sendMessage(Player p, String msg) {
        if (msg == null || msg.equalsIgnoreCase("")) return;
        p.sendMessage(msg);
    }

    public static String replaceText(String str, Pair<String, String>... replaces) {
        str = str.replaceAll("&", "§");
        for (Pair<String, String> pair : replaces) {
            str = str.replaceAll(pair.getKey(), pair.getValue());
        }
        return str;
    }

    private static String getText(String path) {
        FileConfiguration cfg = CustomFoodPlugin.instance.getConfig();
        return (cfg.contains(path)? cfg.getString(path).replaceAll("&", "§").replaceAll("%prefix%", getPrefix()) : null);
    }

    private static String getText(String path, Pair<String, String>... replaces) {
        FileConfiguration cfg = CustomFoodPlugin.instance.getConfig();
        if (cfg.contains(path)) {
            return replaceText(getText(path), replaces);
        } else {
            return null;
        }
    }

    public static ArrayList<String> getList(String path) {
        ArrayList<String> arr = new ArrayList<String>();
        FileConfiguration cfg = CustomFoodPlugin.instance.getConfig();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, new Pair<String, String>("&", "§")));
        }
        return arr;
    }

    public static ArrayList<String> getList(String path, Pair<String, String>... replaces) {
        ArrayList<String> arr = new ArrayList<String>();
        FileConfiguration cfg = CustomFoodPlugin.instance.getConfig();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, replaces));
        }
        return arr;
    }

    public static String getPrefix() {
        FileConfiguration cfg = CustomFoodPlugin.instance.getConfig();
        return cfg.contains("Messages.prefix")? cfg.getString("Messages.prefix").replaceAll("&", "§") : null;
    }

    // Custom Item Inv

    public static String getCustomItemTitle() {
        return getText("Inventories.Custom-Item.Title");
    }

    public static String getSetNameItemName() {
        return getText("Inventories.Custom-Item.set-name.name");
    }

    public static String getSetLoreItemName() {
        return getText("Inventories.Custom-Item.set-lore.name");
    }

    public static String getSetHeadItemName() {
        return getText("Inventories.Custom-Item.set-head.name");
    }

    public static String getSetFoodItemName() {
        return getText("Inventories.Custom-Item.set-food.name");
    }

    public static String getSetSaturationItemName() {
        return getText("Inventories.Custom-Item.set-saturation.name");
    }

    public static String getAddPotionsItemName() {
        return getText("Inventories.Custom-Item.add-potions.name");
    }

    public static ArrayList<String> getSetNameItemLore() {
        return getList("Inventories.Custom-Item.set-name.lore");
    }

    public static ArrayList<String> getSetLoreItemLore() {
        return getList("Inventories.Custom-Item.set-lore.lore");
    }

    public static ArrayList<String> getSetHeadItemLore() {
        return getList("Inventories.Custom-Item.set-head.lore");
    }

    public static String getSaveName() {
        return getText("Inventories.Custom-Item.save.name");
    }

    public static ArrayList<String> getSaveLore() {
        return getList("Inventories.Custom-Item.save.name");
    }

    public static ArrayList<String> getSetFoodItemLore(int value) {
        return getList("Inventories.Custom-Item.set-food.lore", new Pair<>("%food%", ((value > 0)? "§a" : ((value < 0)? "§c" : "§b")) + value));
    }

    public static ArrayList<String> getSetSaturationItemLore(int value) {
        return getList("Inventories.Custom-Item.set-saturation.lore", new Pair<>("%saturation%", ((value > 0)? "§a" : ((value < 0)? "§c" : "§b")) + value));
    }

    // Potions Inv

    public static String getPotionsTitle() {
        return getText("Inventories.Potions.Title");
    }

    public static ArrayList<String> getAddPotionsItemLore() {
        return getList("Inventories.Custom-Item.add-potions.lore");
    }

    public static String getInvalidHeadNameMessage() {
        return getText("Messages.invalid-head-name");
    }

    public static String getPotionsInvItemName(String name, int power, double duration) {
        return getText("Inventories.Potions.name", new Pair<>("%potion%", name), new Pair("%power%", Integer.toString(power)), new Pair<>("%duration%", Double.toString(duration)));
    }

    public static ArrayList<String> getPotionsInvItemLore(String name, int power, double duration) {
        return getList("Inventories.Potions.lore", new Pair<>("%potion%", name), new Pair("%power%", Integer.toString(power)), new Pair<>("%duration%", TimeConverter.convert(duration)));
    }

    public static ArrayList<String> getPotionsInvItemLoreAdd(String name, int power, double duration) {
        return getList("Inventories.Potions.lore-add", new Pair<>("%potion%", name), new Pair("%power%", Integer.toString(power)), new Pair<>("%duration%", TimeConverter.convert(duration)));
    }

    public static ArrayList<String> getPotionsInvItemLoreRemove(String name, int power, double duration) {
        return getList("Inventories.Potions.lore-remove", new Pair<>("%potion%", name), new Pair("%power%", Integer.toString(power)), new Pair<>("%duration%", TimeConverter.convert(duration)));
    }

    public static String getBackName() {
        return getText("Inventories.Potions.back.name");
    }

    public static ArrayList<String> getBackLore() {
        return getList("Inventories.Potions.back.lore");
    }

    // Custom Recipe

    public static String getCustomRecipeTitle() {
        return getText("Inventories.Custom-Recipe.Title");
    }

    public static String getRSaveName() {
        return getText("Inventories.Custom-Recipe.save.name");
    }

    public static ArrayList<String> getRSaveLore() {
        return getList("Inventories.Custom-Recipe.save.name");
    }

    // Messages

    public static String getItemNotHeadMessage() {
        return getText("Messages.not-head-item");
    }

    public static String getNoItemInHandMessage() {
        return getText("Messages.no-item-in-hand");
    }

    public static TextComponent getCommandMessage(String cmd) {
        String msg =  getText("Messages.commands." + cmd);
        TextComponent comp = new TextComponent(msg);
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§eClick to bind")));
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/CustomFood " + cmd));
        return comp;
    }

    public static String getPluginCommandsMessage() {
        return getText("Messages.commands.plugin-commands");
    }

    public static String getConfigReloaded() {
        return getText("Messages.config-reloaded");
    }

    public static ArrayList<String> getCommandAlias() {
        return getList("command-alias");
    }

    public static String getEditedItemExist() {
        return getText("Messages.edited-item-exist");
    }

    public static String getEditedItemNotExist() {
        return getText("Messages.edited-item-not-exist");
    }

    public static String getNoPermsMessage() {
        return getText("Messages.no-perms");
    }

    public static String getRecipeExist() {
        return getText("Messages.recipe-exist");
    }

    public static String getRecipeNotExist() {
        return getText("Messages.recipe-not-exist");
    }

    public static String getOfflinePlayer(String pname) {
        return getText("Messages.player-offline", new Pair<>("%player%", pname));
    }

    public static String getNotAllowedItem() {
        return getText("Messages.not-allowed-item");
    }

    public static String getItemRemoved(String name) {
        return getText("Messages.item-removed", new Pair<>("%name%", name));
    }

    public static String getRecipeRemoved(String name) {
        return getText("Messages.recipe-removed", new Pair<>("%name%", name));
    }

    public static String getSameIngredientsMessage() {
        return getText("Messages.same-ingredients");
    }

    public static String getNoRecipeMessage() {
        return getText("Messages.no-result");
    }

    // Others

    public static void createEditedItemsFile() {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "editedItems.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createRecipesFile() {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "recipes.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean editedNameExist(String name) {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "editedItems.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.contains(name);
    }


    public static void addEditedItem(ItemStack item, String name) {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "editedItems.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(name, item);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean removeEditedItem(String name) {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "editedItems.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (cfg.contains(name)) {
            cfg.set(name, null);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else return false;
    }

    public static ItemStack getEditedItem(String name) {
        File file = new File(CustomFoodPlugin.instance.getDataFolder(), "editedItems.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.contains(name)? cfg.getItemStack(name) : null;
    }
}
