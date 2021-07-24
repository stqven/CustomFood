package com.inv;
// fix on exit item
import com.inv.BetterRecipes.BetterRecipe;
import com.inv.Inventories.CustomItem.CustomItemEvents;
import com.inv.Inventories.CustomRecipe.CraftingEvents;
import com.inv.Inventories.CustomRecipe.CustomRecipeEvents;
import com.inv.Inventories.Potions.PotionsEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class CustomFoodPlugin extends JavaPlugin implements Listener {

    public static CustomFoodPlugin instance;

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getPluginManager().registerEvents(new CustomItemEvents(), this);
        Bukkit.getPluginManager().registerEvents(new CustomRecipeEvents(), this);
        Bukkit.getPluginManager().registerEvents(new PotionsEvents(), this);

        Bukkit.getPluginManager().registerEvents(new CraftingEvents(), this);

        Bukkit.getPluginManager().registerEvents(new CustomFoodCommand(), this);
        Bukkit.getPluginManager().registerEvents(new ItemEvents(), this);

        getCommand("customfood").setExecutor(new CustomFoodCommand());

        System.out.println("[CustomFood] The plugin has been enabled");
        System.out.println("[CustomFood] Developed by inv, nthByte");

        Config.createEditedItemsFile();
        SignInput.listen();
        saveDefaultConfig();
        Config.createRecipesFile();
        BetterRecipe.loadCfgRecipes();
    }

    public void onLoad() {
        instance = this;
    }

    public void onDisable() {
        System.out.println("[CustomFood] The plugin has been disabled");
        System.out.println("[CustomFood] Developed by inv, nthByte");
    }
}
