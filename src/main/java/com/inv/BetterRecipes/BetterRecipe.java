package com.inv.BetterRecipes;

import com.inv.CustomFoodPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BetterRecipe {
    private static ArrayList<BetterRecipe> recipes = new ArrayList<>();
    private static File file = new File(CustomFoodPlugin.instance.getDataFolder(), "recipes.yml");

    String name;
    ItemStack[] matrix;
    ItemStack result;

    public BetterRecipe(ItemStack[] matrix, ItemStack result, String name) {
        this.matrix = matrix;
        this.result = result;
        this.name = name;
        if (!existRecipe(name)) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set(name + ".Items", matrix);
            cfg.set(name + ".Result", result);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        recipes.add(this);
    }

    public boolean remove() {
        if (existRecipe(name)) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set(name, null);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            recipes.remove(this);
            return true;
        } else return false;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack[] getIngredients() {
        return matrix;
    }

    public boolean compareToCrafting(ItemStack[] craftingMatrix) {
        boolean b = true;
        for (int i = 0; i < matrix.length; i++) {
            if (!matrix[i].equals(craftingMatrix[i])) {
                b = false;
            }
        }
        return b;
    }

    public static boolean existRecipe(String name) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.contains(name);
    }

    public static ArrayList<BetterRecipe> getRecipes() {
        return recipes;
    }

    public static BetterRecipe getRecipe(String name) {
        for (BetterRecipe recipe : recipes) {
            if (recipe.getName().equalsIgnoreCase(name)) return recipe;
        }
        return null;
    }

    public static void loadCfgRecipes() {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Set<String> recipesNames = cfg.getConfigurationSection("").getKeys(false);
        for (String recipeName : recipesNames) {
            ItemStack[] recipeItems = (cfg.contains(recipeName + ".Items"))? (((List<ItemStack>) cfg.get(recipeName + ".Items")).toArray(new ItemStack[0])) : new ItemStack[]{};
            ItemStack recipeResult = cfg.contains(recipeName + ".Result")? cfg.getItemStack(recipeName + ".Result") : null;
            new BetterRecipe(recipeItems, recipeResult, recipeName);
        }
    }
}
