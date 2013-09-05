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
	

	@EventHandler( priority = EventPriority.MONITOR )
	public void BlockBreak( BlockBreakEvent event )
	{
		String player = event.getPlayer().getName().toUpperCase();
		
		int[] frequency;
		if ( PlayerOreLog.containsKey( player ))
			frequency = PlayerOreLog.get( player );
		else
			frequency = new int[256];
		
		Block block = event.getBlock();
		int blockId = block.getTypeId();

		if ( blockId == Material.STONE.getId() )
		{
			frequency[blockId]++;
		}
		else if ( ConfiguredBlocks.containsKey( blockId ))
		{
			int[] config = ConfiguredBlocks.get( blockId );
			
			// config[0] is height limit -- REDTAG make this better, not a magic number
			if ( config[0] < 1 )
				frequency[blockId]++;
			else if ( block.getLocation().getBlockY() <= config[0] )
				frequency[blockId]++;
		}

		PlayerOreLog.put( player, frequency );
	}
}
