package com.firstchest.orewatch;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;


public final class Main
{
	public static final int STONE_POS = 0;
	public static final int COAL_POS = 1;
	public static final int IRON_POS = 2;
	public static final int POS_SIZE = 3;
	
	public HashMap<String, int[]> playerOreLog = new HashMap<String, int[]>();


	@EventHandler( priority = EventPriority.MONITOR ) // REDTAG can this be tested?
	public void BlockBreak( BlockBreakEvent event )
	{
		String player = event.getPlayer().getName().toUpperCase();
		
		int frequency[] = new int[POS_SIZE];
		
		Block block = event.getBlock();
		int blockId = block.getTypeId();
		
		if ( blockId == Material.STONE.getId() )
			frequency[STONE_POS]++;
		else if ( blockId == Material.COAL_ORE.getId() )
			frequency[COAL_POS]++;
		else if ( block.getLocation().getBlockY() <= 64 )
			frequency[IRON_POS]++;
		
		playerOreLog.put( player, frequency );
	}
}
