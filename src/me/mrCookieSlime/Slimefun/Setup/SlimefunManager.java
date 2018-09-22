package me.mrCookieSlime.Slimefun.Setup;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlimefunManager {
	
	public static SlimefunStartup plugin;
	public static String PREFIX;
	public static Map<EntityType, List<ItemStack>> drops = new HashMap<EntityType, List<ItemStack>>();
	
	public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, PotionEffect[][] effects, boolean special, boolean slimefun) {
		String[] components = new String[] {"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
		Category cat = special ? Categories.MAGIC_ARMOR: Categories.ARMOR;
		List<ItemStack[]> recipes = new ArrayList<ItemStack[]>();
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
		recipes.add(new ItemStack[] {baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		recipes.add(new ItemStack[] {null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		for (int i = 0; i < 4; i++) {
			if ((effects.length - 1) >= i) if (effects[i].length > 0) new SlimefunArmorPiece(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i), effects[i]).register(slimefun);
			else new SlimefunItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
		}
	}
	
	public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, boolean slimefun, boolean vanilla) {
		String[] components = new String[] {"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
		Category cat = Categories.ARMOR;
		List<ItemStack[]> recipes = new ArrayList<ItemStack[]>();
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
		recipes.add(new ItemStack[] {baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
		recipes.add(new ItemStack[] {baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		recipes.add(new ItemStack[] {null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
		for (int i = 0; i < 4; i++) {
			if (vanilla) {
				new VanillaItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
			} else {
				new SlimefunItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(slimefun);
			}
		}
	}

	//ToDO: ALl all
	//Charcoal=coal?
//	public static List<Material> data_safe = Arrays.asList(Material.WHITE_WOOL,
//			Material.WHITE_CARPET,
//			Material.WHITE_TERRACOTTA,
//			Material.WHITE_STAINED_GLASS,
//			Material.WHITE_STAINED_GLASS_PANE,
//			Material.INK_SAC,
//			Material.STONE,
//			Material.COAL, Material.SKULL_ITEM, Material.RAW_FISH, Material.COOKED_FISH);
	
	public static boolean isItemSimiliar(ItemStack item, ItemStack SFitem, boolean lore) {
		return isItemSimiliar(item, SFitem, lore, DataType.IF_COLORED);
	}
	
	public static enum DataType {
		
		ALWAYS,
		NEVER,
		IF_COLORED;
		
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isItemSimiliar(ItemStack item, ItemStack SFitem, boolean lore, DataType data) {
		return SlimefunStartup.instance.myTitanHooks.isItemSimiliar(item, SFitem, lore,data);
	}
	
	public static boolean equalsLore(List<String> lore, List<String> lore2) {
		return SlimefunStartup.instance.myTitanHooks.equalsLore(lore, lore2);
		/*
		String string1 = "", string2 = "";
		for (String string: lore) {
			if (!string.startsWith("&e&e&7")) string1 = string1 + "-NEW LINE-" + string;
		}
		for (String string: lore2) {
			if (!string.startsWith("&e&e&7")) string2 = string2 + "-NEW LINE-" + string;
		}
		return string1.equals(string2);*/
	}
}
