package me.mrCookieSlime.Slimefun.URID;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class AutoSavingTask implements Runnable {
	
	@Override
	public void run() {
		SlimefunStartup.instance.myTitanHooks.saveBackuptoFile();
		SlimefunStartup.instance.myTitanHooks.clearBackupfromFile();

		Set<BlockStorage> worlds = new HashSet<BlockStorage>();
		
		for (World world: Bukkit.getWorlds()) {
			if (BlockStorage.isWorldRegistered(world.getName())) {
				BlockStorage storage = BlockStorage.getStorage(world);
				storage.computeChanges();
				
				if (storage.getChanges() > 0) {
					worlds.add(storage);
				}
			}
		}
		
		if (!worlds.isEmpty()) {
			System.out.println("[Slimefun] Auto-Saving Data... (Next Auto-Save: " + SlimefunStartup.getCfg().getInt("options.auto-save-delay-in-minutes") + "m)");
			
			for (BlockStorage storage: worlds) {
				storage.save(false);
			}
		}
	}

}
