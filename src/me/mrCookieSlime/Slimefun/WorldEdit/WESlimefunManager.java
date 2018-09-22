package me.mrCookieSlime.Slimefun.WorldEdit;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;

//import com.sk89q.worldedit.blocks.BaseBlock;
//import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;

public class WESlimefunManager {
	
	public WESlimefunManager() {
		WorldEdit.getInstance().getEventBus().register(this);
	}
	
	@Subscribe
    public void wrapForLogging(final EditSessionEvent event) {
		/*event.setExtent(new AbstractLoggingExtent(event.getExtent()) {
			
			@Override
			protected void onBlockChange(Vector pos, BaseBlock b) {
				super.onBlockChange(pos, b);
				
				if (b.getType() == 0) {
					World world = Bukkit.getWorld(event.getWorld().getName());
					
					if (world != null) {
						Location l = new Location(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
						if (BlockStorage.hasBlockInfo(l)) BlockStorage.clearBlockInfo(l);
					}
				}
			}
			
		});*/
    }

}
