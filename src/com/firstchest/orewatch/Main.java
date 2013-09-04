package com.firstchest.orewatch;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;


public final class Main
{
	public HashMap<String, int[]> PlayerOreLog = new HashMap<String, int[]>();
	public HashMap<Integer, int[]> ConfiguredBlocks = new HashMap<Integer, int[]>();
	

	@EventHandler( priority = EventPriority.MONITOR ) // REDTAG can this be tested?
	public void BlockBreak( BlockBreakEvent event )
	{
		String player = event.getPlayer().getName().toUpperCase();
		
		int frequency[] = new int[256];
		
		Block block = event.getBlock();
		int blockId = block.getTypeId();

		if ( blockId == Material.STONE.getId() )
			frequency[blockId]++;
		else if ( ConfiguredBlocks.containsKey( blockId ))
			frequency[blockId]++;

//		else if ( block.getLocation().getBlockY() <= 64 )
//			frequency[IRON_POS]++;
		
		PlayerOreLog.put( player, frequency );
	}
}
