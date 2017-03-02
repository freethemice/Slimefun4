package me.mrCookieSlime.Slimefun.api.item_transport;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import java.util.Comparator;

public class RecipeSorter implements Comparator<Integer> {
	
	BlockMenu menu;
	
	public RecipeSorter(BlockMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public int compare(Integer slot1, Integer slot2) {
		return SlimefunStartup.instance.myTitanHooks.compare(menu, slot1,slot2);
		//return menu.getItemInSlot(slot1).getAmount() - menu.getItemInSlot(slot2).getAmount();
	}

}
