package com.inv;

import com.inv.BetterRecipes.BetterRecipe;
import com.inv.Inventories.CustomItem.CustomItemInventory;
import com.inv.Inventories.CustomRecipe.CustomRecipeInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CustomFoodCommand implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("customfood")) {
            Player p = (Player) sender;
            if (!p.hasPermission("customfood.admin")) {
                Config.sendMessage(p, Config.getNoPermsMessage());
                return false;
            }
            if (args.length == 0) {
                p.sendMessage(" ");
                Config.sendMessage(p,Config.getPluginCommandsMessage());
                p.spigot().sendMessage(Config.getCommandMessage("editItem"));
                p.spigot().sendMessage(Config.getCommandMessage("craft"));
                p.spigot().sendMessage(Config.getCommandMessage("give"));
                p.spigot().sendMessage(Config.getCommandMessage("removeItem"));
                p.spigot().sendMessage(Config.getCommandMessage("removeRecipe"));
                p.spigot().sendMessage(Config.getCommandMessage("reload"));
            } else {
                if (args[0].equalsIgnoreCase("editItem")) {
                    if (args.length == 1) {
                        p.spigot().sendMessage(Config.getCommandMessage("editItem"));
                        return false;
                    }
                    String name = args[1].toLowerCase();
                    if (Config.editedNameExist(name)) {
                        p.openInventory(CustomItemInventory.getInventory(Config.getEditedItem(name), name));
                        return false;
                    }
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item == null || item.getType() == Material.AIR) {
                        Config.sendMessage(p, Config.getNoItemInHandMessage());
                        return false;
                    }
                    if (!item.getType().isEdible() && item.getType() != Material.POTION && item.getType() != Material.PLAYER_HEAD) {
                        Config.sendMessage(p, Config.getNotAllowedItem());
                        return false;
                    }
                    item = p.getInventory().getItemInMainHand().clone();
                    p.getInventory().setItemInMainHand(null);
                    p.openInventory(CustomItemInventory.getInventory(item, name));
                } else if (args[0].equalsIgnoreCase("craft")) {
                    if (args.length == 1) {
                        p.spigot().sendMessage(Config.getCommandMessage("craft"));
                        return false;
                    }
                    String name = args[1].toLowerCase();
                    if (!BetterRecipe.existRecipe(name)) {
                        p.openInventory(CustomRecipeInventory.getInventory(name));
                    } else {
                        Config.sendMessage(p, Config.getRecipeExist());
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (args.length == 1) {
                        p.spigot().sendMessage(Config.getCommandMessage("give"));
                        return false;
                    }
                    String iname = args[1].toLowerCase(), pname = null;
                    if (args.length == 3) {
                        pname = args[2];
                    }
                    ItemStack item = Config.getEditedItem(iname);
                    if (item == null) {
                        Config.sendMessage(p, Config.getEditedItemNotExist());
                        return false;
                    }
                    if (pname != null) {
                        Player t = Bukkit.getPlayer(pname);
                        if (t != null) {
                            t.getInventory().addItem(item);
                        } else {
                            Config.sendMessage(p, Config.getOfflinePlayer(pname));
                        }
                    } else {
                        p.getInventory().addItem(item);
                    }
                } else if (args[0].equalsIgnoreCase("removeItem")) {
                    if (args.length <= 1) {
                        p.spigot().sendMessage(Config.getCommandMessage("removeItem"));
                        return false;
                    }
                    String name = args[1].toLowerCase();
                    if (Config.removeEditedItem(name)) {
                        Config.sendMessage(p, Config.getItemRemoved(name));
                    } else {
                        Config.sendMessage(p, Config.getEditedItemNotExist());
                    }
                } else if (args[0].equalsIgnoreCase("removeRecipe")) {
                    if (args.length <= 1) {
                        p.spigot().sendMessage(Config.getCommandMessage("removeRecipe"));
                        return false;
                    }
                    String name = args[1].toLowerCase();
                    BetterRecipe recipe = BetterRecipe.getRecipe(name);
                    if (recipe != null && recipe.remove()) {
                        Config.sendMessage(p, Config.getRecipeRemoved(name));
                    } else {
                        Config.sendMessage(p, Config.getRecipeNotExist());
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    CustomFoodPlugin.instance.reloadConfig();
                    Config.sendMessage(p, Config.getConfigReloaded());
                } else {
                    p.sendMessage(" ");
                    Config.sendMessage(p, Config.getPluginCommandsMessage());
                    p.spigot().sendMessage(Config.getCommandMessage("editItem"));
                    p.spigot().sendMessage(Config.getCommandMessage("craft"));
                    p.spigot().sendMessage(Config.getCommandMessage("give"));
                    p.spigot().sendMessage(Config.getCommandMessage("removeItem"));
                    p.spigot().sendMessage(Config.getCommandMessage("removeRecipe"));
                    p.spigot().sendMessage(Config.getCommandMessage("reload"));
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onCMD(PlayerCommandPreprocessEvent e) {
        ArrayList<String> alias = Config.getCommandAlias();
        for (String as : alias) {
            String nmsg = ("/" + as).replaceAll(" ", "");
            if (e.getMessage().startsWith(nmsg)) {
                e.setMessage(e.getMessage().replaceFirst(nmsg, "/customfood"));
                break;
            }
        }
    }
}
